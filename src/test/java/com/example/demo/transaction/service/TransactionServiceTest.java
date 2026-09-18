package com.example.demo.transaction.service;

import com.example.demo.account.Account;
import com.example.demo.account.AccountMapper;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.transaction.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// data.sql 시드 데이터 기준
// 계좌 1111(id=1, memberId=1, balance=1200), 2222(id=2, memberId=2, balance=1500), 3333(id=3, memberId=3, balance=0)
// 모든 계좌 비밀번호(평문)는 "1234"
@SpringBootTest
@Transactional
class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

    @MockitoSpyBean
    AccountMapper accountMapper;

    private TransferRequest request(Long fromAccountId, String toAccountNumber, long amount, String password) {
        TransferRequest req = new TransferRequest();
        req.setFromAccountId(fromAccountId);
        req.setToAccountNumber(toAccountNumber);
        req.setAmount(BigDecimal.valueOf(amount));
        req.setPassword(password);
        return req;
    }

    @Test
    void 정상_이체시_잔액이_이동한다() throws BuisinessException {
        transactionService.transfer(request(1L, "2222", 100, "1234"), 1L);

        Account from = accountMapper.findById(1L);
        Account to = accountMapper.findById(2L);

        assertThat(from.getBalance()).isEqualByComparingTo("1100");
        assertThat(to.getBalance()).isEqualByComparingTo("1600");
    }

    @Test
    void 잔액보다_많은_금액을_이체하면_예외() {
        assertThatThrownBy(() -> transactionService.transfer(request(1L, "2222", 100_000, "1234"), 1L))
                .isInstanceOf(BuisinessException.class)
                .extracting(e -> ((BuisinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INSUFFICIENT_BALANCE);
    }

    @Test
    void 계좌_비밀번호가_틀리면_예외() {
        assertThatThrownBy(() -> transactionService.transfer(request(1L, "2222", 100, "wrong"), 1L))
                .isInstanceOf(BuisinessException.class)
                .extracting(e -> ((BuisinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_ACCOUNT_PASSWORD);
    }

    @Test
    void 존재하지_않는_수취계좌면_예외() {
        assertThatThrownBy(() -> transactionService.transfer(request(1L, "9999", 100, "1234"), 1L))
                .isInstanceOf(BuisinessException.class)
                .extracting(e -> ((BuisinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Test
    void 본인에게_이체하면_예외() {
        assertThatThrownBy(() -> transactionService.transfer(request(1L, "1111", 100, "1234"), 1L))
                .isInstanceOf(BuisinessException.class)
                .extracting(e -> ((BuisinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.SELF_TRANSFER_NOT_ALLOWED);
    }

    @Test
    void 남의_계좌에서_이체하면_예외() {
        // fromAccountId=1(1111)은 memberId=1(gildong) 소유인데 memberId=2로 호출
        assertThatThrownBy(() -> transactionService.transfer(request(1L, "2222", 100, "1234"), 2L))
                .isInstanceOf(BuisinessException.class)
                .extracting(e -> ((BuisinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.NOT_ACCOUNT_OWNER);
    }

    @Test
    void 이체금액이_0이하면_예외() {
        assertThatThrownBy(() -> transactionService.transfer(request(1L, "2222", 0, "1234"), 1L))
                .isInstanceOf(BuisinessException.class)
                .extracting(e -> ((BuisinessException) e).getErrorCode())
                .isEqualTo(ErrorCode.INVALID_AMOUNT);
    }

    @Test
    void 이체_중간_예외시_전체_롤백() {
        BigDecimal before1 = accountMapper.findById(1L).getBalance();
        BigDecimal before2 = accountMapper.findById(2L).getBalance();

        doThrow(new RuntimeException("강제"))
                .when(accountMapper).updateBalance(eq(2L), any());

        // 테스트가 열어둔 트랜잭션을 여기서 커밋하고 끝냄
        // -> transfer()가 독립된 진짜 트랜잭션에서 실행되게 함
        TestTransaction.flagForCommit();
        TestTransaction.end();

        assertThatThrownBy(() -> transactionService.transfer(request(1L, "2222", 300, "1234"), 1L))
                .isInstanceOf(RuntimeException.class);

        // 새 트랜잭션을 열어서 실제로 DB에 반영된(혹은 롤백된) 값을 확인
        TestTransaction.start();
        assertThat(accountMapper.findById(1L).getBalance()).isEqualByComparingTo(before1);
        assertThat(accountMapper.findById(2L).getBalance()).isEqualByComparingTo(before2);
    }


    @Test
    void AtoB와_BtoA를_동시에_해도_데드락_없이_처리된다() throws Exception {
        int perDirection = 4;              // A->B 4개, B->A 4개 = 총 8개 (기본 커넥션풀 10개보다 여유있게)
        int threadCount = perDirection * 2;
        BigDecimal amount = BigDecimal.valueOf(10);

        // 워커 스레드가 각자 독립된 진짜 트랜잭션으로 동작하도록,
        // 메인 테스트가 들고 있던 트랜잭션은 먼저 커밋하고 끝냄 (커넥션도 반납됨)
        TestTransaction.flagForCommit();
        TestTransaction.end();

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            boolean aToB = i % 2 == 0;
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    if (aToB) {
                        transactionService.transfer(request(1L, "2222", 10, "1234"), 1L);
                    } else {
                        transactionService.transfer(request(2L, "1111", 10, "1234"), 2L);
                    }
                } catch (Throwable e) {
                    errors.add(e);
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();                              // 모든 스레드가 대기 상태로 모일 때까지
        start.countDown();                           // 동시에 출발
        boolean finishedInTime = done.await(10, TimeUnit.SECONDS); // 데드락이면 여기서 타임아웃
        executor.shutdownNow();

        assertThat(finishedInTime).as("데드락으로 인해 타임아웃 발생").isTrue();
        assertThat(errors).as("예상치 못한 예외 발생: " + errors).isEmpty();

        // 검증용 새 트랜잭션 - A->B 4번, B->A 4번, 각 10원씩이라 순잔액 변화는 0이어야 함
        TestTransaction.start();
        assertThat(accountMapper.findById(1L).getBalance()).isEqualByComparingTo("1200");
        assertThat(accountMapper.findById(2L).getBalance()).isEqualByComparingTo("1500");
    }
}
