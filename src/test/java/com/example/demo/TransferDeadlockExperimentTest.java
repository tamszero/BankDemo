
 package com.example.demo;

import com.example.demo.account.AccountMapper;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.transaction.dto.TransferRequest;
import com.example.demo.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/*
 * ============================================================================
 *  [실험용] 데드락 재현 테스트
 * ============================================================================
 *  목적: "락 순서를 정렬하지 않으면 실제로 데드락이 난다"는 것을 눈으로 증명하는 테스트입니다.
 *  평소 빌드/CI에서는 실행되지 않도록 @Disabled 를 걸어뒀습니다. (동시성 타이밍에 의존하는
 *  테스트라 결과가 매번 100% 똑같이 나오지 않을 수 있어서, 정식 회귀 테스트로는 부적합합니다.)
 *
 *  실행 방법:
 *   1) TransactionService.transfer() 안의 락 거는 부분을, 요청 순서 그대로 잠그도록
 *      "일부러" 아래처럼 바꿉니다. (원래는 계좌 ID가 작은 쪽을 먼저 잠그도록 정렬되어 있어야 정상)
 *
 *      from = accountMapper.findByIdForUpdate(from.getId());
 *      to   = accountMapper.findByIdForUpdate(to.getId());
 *
 *   2) 클래스 위의 @Disabled를 잠깐 주석 처리하거나, IDE에서 이 테스트만 우클릭 실행합니다.
 *   3) 콘솔에 "실패: CannotAcquireLockException ... Deadlock found when trying to get lock"
 *      같은 메시지가 찍히는지 확인합니다. → 데드락이 실제로 재현된 것입니다.
 *   4) 확인이 끝나면 1)에서 바꾼 코드를 원래대로(락 순서 정렬) 되돌리고,
 *      이 실험용 테스트는 다시 @Disabled 상태로 둔 채, TransferDeadlockTest(정상용)로
 *      회귀 테스트를 돌리세요.
 * ============================================================================
 */
@SpringBootTest
//@Disabled("일부러 데드락을 재현하는 실험용 테스트. transfer()의 락 순서를 실험 버전으로 바꾼 뒤 수동으로만 실행할 것")
@DisplayName("[실험] 락 순서를 정렬하지 않으면 데드락이 난다")
class TransferDeadlockExperimentTest {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper.updateBalance(1L, new BigDecimal("100000"));
        accountMapper.updateBalance(2L, new BigDecimal("100000"));
    }

    @Test
    @DisplayName("🔥 A→B와 B→A를 동시에 실행하면 데드락으로 일부가 실패한다")
    void 양방향_동시이체_데드락_재현() throws InterruptedException {

        int pairs = 20;
        ExecutorService executor = Executors.newFixedThreadPool(16);
        CountDownLatch done = new CountDownLatch(pairs * 2);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();

        String acc1 = accountMapper.findById(1L).getAccountNumber();
        String acc2 = accountMapper.findById(2L).getAccountNumber();

        for (int i = 0; i < pairs; i++) {
            executor.submit(() -> run(() -> {
                        try {
                            transactionService.transfer(
                                    new TransferRequest(1L, acc2, new BigDecimal("100"), "1234"), 1L);
                        } catch (BuisinessException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    success, failure, done));
            executor.submit(() -> run(() -> {
                        try {
                            transactionService.transfer(
                                    new TransferRequest(2L, acc1, new BigDecimal("100"), "1234"), 2L);
                        } catch (BuisinessException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    success, failure, done));
        }

        boolean finished = done.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        BigDecimal total = accountMapper.findById(1L).getBalance()
                .add(accountMapper.findById(2L).getBalance());

        System.out.println("완료: " + finished + " / 성공 " + success.get()
                + " / 실패 " + failure.get());
        System.out.println("두 계좌 합계: " + total);

        // 데드락이 나더라도 MySQL이 victim을 골라 강제로 풀어주기 때문에 앱이 영원히 멈추진 않는다
        assertThat(finished).isTrue();

        // ⭐ 이 테스트의 핵심 assertion: 락 순서가 안전하지 않으면 반드시 몇 건은 실패해야 한다.
        //    (실패 = 0건이면 오히려 "이 실험이 재현에 실패했다"는 뜻이니, 스레드 수/반복 횟수를 늘려보세요)
        assertThat(failure.get())
                .as("락 순서를 정렬하지 않았으므로 데드락으로 인한 실패가 최소 1건은 있어야 한다")
                .isGreaterThan(0);

        // 실패한 트랜잭션은 전액 롤백되므로, 성공/실패 건수와 무관하게 총액은 항상 보존되어야 한다
        assertThat(total)
                .as("실패한 이체가 있어도 부분 반영 없이 원자적으로 롤백됐다면 총액은 그대로여야 한다")
                .isEqualByComparingTo(new BigDecimal("200000"));
    }

    private void run(Runnable task, AtomicInteger s, AtomicInteger f, CountDownLatch d) {
        try {
            task.run();
            s.incrementAndGet();
        } catch (Exception e) {
            f.incrementAndGet();
            System.out.println("실패: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        } finally {
            d.countDown();
        }
    }
}



