package com.example.demo.concurrency;

import com.example.demo.account.AccountMapper;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.transaction.dto.WithdrawRequest;
import com.example.demo.transaction.service.TransactionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("동시 출금 시 잔액 정합성")
class WithdrawConcurrencyTest {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountMapper accountMapper;

    static final Long ACCOUNT_ID = 1L;
    static final Long MEMBER_ID  = 1L;
    static final BigDecimal INITIAL = new BigDecimal("10000");
    static final BigDecimal AMOUNT  = new BigDecimal("1000");
    static final int THREADS = 10;

    @BeforeEach
    void setUp() {
        accountMapper.updateBalance(ACCOUNT_ID, INITIAL);
    }

    @Test
    @DisplayName("❌ 락 없이: 갱신 손실이 발생한다")
    void 락_없이_동시출금() throws InterruptedException {

        var result = ConcurrencyTestSupport.runConcurrently(THREADS, () ->
        {
            try {
                transactionService.withdrawWithoutLock(
                        new WithdrawRequest(ACCOUNT_ID, AMOUNT, "1234"), MEMBER_ID);
            } catch (BuisinessException e) {
                throw new RuntimeException(e);
            }
        });

        BigDecimal finalBalance = accountMapper.findById(ACCOUNT_ID).getBalance();
        BigDecimal expected = INITIAL.subtract(AMOUNT.multiply(BigDecimal.valueOf(result.success())));

        System.out.println("=".repeat(60));
        System.out.println("  [락 없음]");
        System.out.println("  초기 잔액   : " + INITIAL);
        System.out.println("  요청        : " + THREADS + "건 x " + AMOUNT);
        System.out.println("  성공/실패   : " + result.success() + " / " + result.failure());
        System.out.println("  기대 잔액   : " + expected);
        System.out.println("  실제 잔액   : " + finalBalance);
        System.out.println("  차이(유실)  : " + finalBalance.subtract(expected));
        System.out.println("=".repeat(60));

        // 이 테스트는 "깨지는 것"을 보여주는 게 목적이므로 단언하지 않는다
    }

    @Test
    @DisplayName("✅ 락 적용: 잔액이 정확하다")
    void 락_적용_동시출금() throws InterruptedException {

        var result = ConcurrencyTestSupport.runConcurrently(THREADS, () ->
        {
            try {
                transactionService.withdraw(
                        new WithdrawRequest(ACCOUNT_ID, AMOUNT, "1234"), MEMBER_ID);
            } catch (BuisinessException e) {
                throw new RuntimeException(e);
            }
        });

        BigDecimal finalBalance = accountMapper.findById(ACCOUNT_ID).getBalance();
        BigDecimal expected = INITIAL.subtract(AMOUNT.multiply(BigDecimal.valueOf(result.success())));

        System.out.println("=".repeat(60));
        System.out.println("  [FOR UPDATE 적용]");
        System.out.println("  성공/실패   : " + result.success() + " / " + result.failure());
        System.out.println("  기대 잔액   : " + expected);
        System.out.println("  실제 잔액   : " + finalBalance);
        System.out.println("  소요        : " + result.elapsedMs() + "ms");
        System.out.println("=".repeat(60));

        assertThat(finalBalance).isEqualByComparingTo(expected);
        assertThat(finalBalance).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.success()).isEqualTo(10);
    }

    @Test
    @DisplayName("✅ 잔액을 초과하는 동시 출금은 일부만 성공한다")
    void 잔액초과_동시출금() throws InterruptedException {
        accountMapper.updateBalance(ACCOUNT_ID, new BigDecimal("5000"));

        var result = ConcurrencyTestSupport.runConcurrently(10, () ->
        {
            try {
                transactionService.withdraw(
                        new WithdrawRequest(ACCOUNT_ID, AMOUNT, "1234"), MEMBER_ID);
            } catch (BuisinessException e) {
                throw new RuntimeException(e);
            }
        });

        BigDecimal finalBalance = accountMapper.findById(ACCOUNT_ID).getBalance();

        System.out.println("성공 " + result.success() + " / 실패 " + result.failure()
                + " → 잔액 " + finalBalance);

        assertThat(result.success()).isEqualTo(5);      // 5건만 성공
        assertThat(result.failure()).isEqualTo(5);      // 5건은 잔액부족
        assertThat(finalBalance).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(finalBalance).isNotNegative();       // ⭐ 마이너스 통장 없음
    }
}