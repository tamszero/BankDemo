package com.example.demo.account;


import com.example.demo.account.dto.AccountCreateRequest;
import com.example.demo.account.dto.AccountResponse;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.util.AccountNumberGenerator;
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

    @Transactional
    public Long create(AccountCreateRequest req, Long memberId) throws BuisinessException {
        Account account = Account.builder()
                .accountNumber(accountNumberGenerator.generate())
                .password(passwordEncoder.encode(req.getPassword()))
                .balance(BigDecimal.ZERO)
                .memberId(memberId)
                .build();
        accountMapper.insert(account);
        return account.getId();
    }
    //회원 아이디로 계좌 목록 조회 -> 리스트 반환
    @Transactional(readOnly = true)
    public List<AccountResponse> findMyAccounts(Long memberId){
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

}
