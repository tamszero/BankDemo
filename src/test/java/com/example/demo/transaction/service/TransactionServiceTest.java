package com.example.demo.transaction.service;

import com.example.demo.account.Account;
import com.example.demo.account.AccountMapper;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.transaction.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

    @Autowired
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
                .isEqualTo(ErrorCode.ACCOUNT_NOT_FOUNT);
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
}
