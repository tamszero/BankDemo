package com.example.demo.transaction;

import com.example.demo.account.Account;
import com.example.demo.account.AccountMapper;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.transaction.dto.DepositRequest;
import com.example.demo.transaction.dto.WithdrawRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountMapper accountMapper;
    private final HistoryMapper historyMapper;
    private final PasswordEncoder passwordEncoder;


    /**
     *
     * <검증 로직>
     *
     */

    // amount가 0이거나 0보다 작을 때
    private void validateAmount(BigDecimal amount) throws BuisinessException {
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new BuisinessException(ErrorCode.INVALID_AMOUNT);
        }
    }
    // 계좌가 null이거나 memberId가 계쫘 주인이 아닐 때
    private void validateOwner(Account account, Long memberId) throws BuisinessException{
        if(account == null){
            throw new BuisinessException(ErrorCode.ACCOUNT_NOT_FOUNT);
        }
        if(!account.getMemberId().equals(memberId)){
            throw new BuisinessException(ErrorCode.NOT_ACCOUNT_OWNER);
        }
    }
    //계좌 비밀번호가 입력한 비밀번호와 다를 때
    private void validateAccountPassword(Account account, String rawPassword) throws BuisinessException{
        if(!passwordEncoder.matches(rawPassword, account.getPassword())){
            throw new BuisinessException(ErrorCode.INVALID_ACCOUNT_PASSWORD);
        }
    }
    //balance가 amount보다 작을 때
    private void validateBalance(Account account, BigDecimal amount) throws BuisinessException{
        if(account.getBalance().compareTo(amount) < 0){
            throw new BuisinessException(ErrorCode.INSUFFICIENT_BALANCE);
        }
    }

    /**
     *
     * <withdraw : 출금, 잔액과 계쫘 비밀번호를 검증한 뒤 금액을 빼고 원장에 기록한다>
     *
     */
    @Transactional
    public void withdraw(WithdrawRequest req, Long memberId) throws BuisinessException {

        validateAmount(req.getAmount());

        //1. 락 걸고 조회
        Account account = accountMapper.findByIdForUpdate(req.getAccountId());

        //2. 검증
        validateOwner(account, memberId);
        validateAccountPassword(account, req.getPassword());
        validateBalance(account, req.getAmount());


        //3. 출금 금액 빼기
        BigDecimal newBalance = account.getBalance().subtract(req.getAmount());

        //4. 원장 기록 + 잔액 갱신 (한 트랜잭션)
        historyMapper.insert(History.builder()
                .txType("WITHDRAW")
                .amount(req.getAmount())
                .withdrawAccountId(account.getId())
                .withdrawBalance(newBalance)
                .build());

        accountMapper.updateBalance(account.getId(),newBalance);
    }




    /**
     *
     * <deposit : 입금, 계좌에 금액을 더하고 원장에 기록>
     *
     */
    @Transactional
    public void deposit(DepositRequest req, Long memberId) throws BuisinessException {

        validateAmount(req.getAmount());

        //락 걸고 조회
        Account account = accountMapper.findByIdForUpdate(req.getAccountId());
        validateOwner(account, memberId);
        validateAccountPassword(account, req.getPassword());

        //입금 금액 더하기
        BigDecimal newBalance = account.getBalance().add(req.getAmount());

        // 원장 기록 -> 잔액 갱신
        historyMapper.insert(History.builder()
                .txType("DEPOSIT")
                .amount(req.getAmount())
                .depositAccountId(account.getId())
                .depositBalance(newBalance)
                .build());

        accountMapper.updateBalance(account.getId(), newBalance);

    }

    /**
     * History 내역 조회
     */
    @Transactional
    public List<History> findHistories(Long accountId, Long memberId) throws BuisinessException{
        Account account = accountMapper.findById(accountId); //단순 조회만하기위해
        validateOwner(account, memberId); // 남의계좌 보지 못하도록 검증

        return historyMapper.findByAccountId(accountId);
    }

}
