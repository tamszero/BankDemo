package com.example.demo.common.util;

import com.example.demo.account.AccountMapper;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class AccountNumberGenerator {

    private final AccountMapper accountMapper;
    private static final String PREFIX = "110";

    public String generate() throws BuisinessException {
        for(int i = 0; i < 10; i++){
            //랜덤 + 중복확인 + 재시도
            //String.format("%09d") -> 앞자리 0을 살리기 위해
            String candidate = PREFIX + String.format("%09d", ThreadLocalRandom.current().nextInt(1_000_000_000));
            if(accountMapper.countByAccountNumber(candidate) == 0){
                return candidate;
            }
        }
        throw new BuisinessException(ErrorCode.ACCOUNT_NUMBER_GENERATION_FAILED);
    }
}
