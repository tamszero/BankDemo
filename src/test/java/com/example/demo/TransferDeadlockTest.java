package com.example.demo;

import com.example.demo.account.AccountMapper;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.transaction.dto.TransferRequest;
import com.example.demo.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
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
 *  [정상용] 데드락 방지 회귀 테스트
 * ============================================================================
 *  전제 조건: TransactionService.transfer()가 두 계좌를 잠글 때, 요청 방향(from/to)이
 *  아니라 "계좌 ID가 작은 쪽을 항상 먼저" 잠그도록 정렬되어 있어야 합니다. 즉:
 *
 *      Long firstId  = Math.min(fromId, toId);
 *      Long secondId = Math.max(fromId, toId);
 *      Account first  = accountMapper.findByIdForUpdate(firstId);
 *      Account second = accountMapper.findByIdForUpdate(secondId);
 *
 *  이렇게 모든 스레드가 "항상 같은 순서로" 락을 걸면, A→B든 B→A든 두 스레드가 잠그는
 *  순서가 같아져서 서로 상대방을 기다리는 원형 대기(circular wait)가 애초에 생기지 않습니다.
 *  그래서 이 테스트는 실패(데드락) 없이 전부 성공해야 정상입니다.
 *
 *  TransferDeadlockExperimentTest와 짝을 이루는 테스트입니다:
 *   - Experiment 버전: 락 순서를 일부러 깨서 "데드락이 실제로 난다"를 증명 (수동 실행 전용)
 *   - 이 버전(정상용): 락 순서를 고친 뒤 "데드락이 안 난다"를 계속 보장 (CI에서 항상 실행)
 * ============================================================================
 */
@SpringBootTest
@DisplayName("이체 데드락 방지")
class TransferDeadlockTest {

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
    @DisplayName("✅ A→B와 B→A를 동시에 실행해도 데드락 없이 전부 성공한다")
    void 양방향_동시이체() throws InterruptedException {

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

        assertThat(finished).isTrue();                                     // 교착 없이 종료 ⭐
        assertThat(failure.get())
                .as("락 순서가 안전하게 정렬되어 있다면 데드락으로 인한 실패가 없어야 한다")
                .isZero();                                                  // 전부 성공 ⭐
        assertThat(total).isEqualByComparingTo(new BigDecimal("200000"));   // 총액 보존 ⭐
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



