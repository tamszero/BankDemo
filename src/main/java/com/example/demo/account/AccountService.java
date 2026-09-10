package com.example.demo.account;


import com.example.demo.account.dto.AccountCreateRequest;
import com.example.demo.account.dto.AccountResponse;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.util.AccountNumberGenerator;
import com.example.demo.account.dto.TransferTargetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final AccountNumberGenerator accountNumberGenerator;

    //계좌 개설
    @Transactional
    public Long create(AccountCreateRequest req, Long memberId) throws BuisinessException {
        if(!req.getPassword().equals(req.getPasswordConfirm())){
            throw new BuisinessException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        Account account = Account.builder()
                .accountNumber(accountNumberGenerator.generate())
                .password(passwordEncoder.encode(req.getPassword()))
                .balance(BigDecimal.ZERO)
                .memberId(memberId)
                .build();
        accountMapper.insert(account);
        return account.getId();
    }
    //가지고있는 계좌 리스트 조회
    @Transactional(readOnly = true)
    public List<AccountResponse> findMyAccounts(Long memberId) throws BuisinessException{
       if (memberId == null){
           throw new BuisinessException(ErrorCode.MEMBER_NOT_FOUND);
       }
        return accountMapper.findByMemberId(memberId).stream()
                .map(AccountResponse::from)
                .toList();
    }

    //회원 아이디, 계좌아이디로 계좌 조회
    @Transactional(readOnly = true)
    public AccountResponse findMyAccount(Long accountId, Long memberId) throws BuisinessException {
        Account account = accountMapper.findById(accountId); //계좌 찾기

        if(account == null){ //없으면 에러 처리
            throw new BuisinessException(ErrorCode.ACCOUNT_NOT_FOUNT);
        }if(!account.getMemberId().equals(memberId)){ //소유자 검증 !! -> 남의 계좌면 에러 처리
            throw new BuisinessException(ErrorCode.NOT_ACCOUNT_OWNER);
        }return AccountResponse.from(account);
    }

    // 계좌번호로 송금할 대상 account 찾기
    @Transactional
    public TransferTargetResponse findTransferTarget(String accountNumber) throws BuisinessException{
        TransferTargetResponse target = accountMapper.findTransferTargetByAccountNumber(accountNumber);

        if(target == null){
            throw new BuisinessException(ErrorCode.ACCOUNT_NOT_FOUNT);
        }
        return target;
    }

}
