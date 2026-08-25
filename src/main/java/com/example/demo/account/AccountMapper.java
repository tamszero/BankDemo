package com.example.demo.account;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {
    int insert(Account account);
    List<Account> findByMemberId(Long memberId);
    Account findById(Long id);
    Account findByAccountNumber(String accountNumber); //수취인 계좌 찾기
    int countByAccountNumber(String accountNumber); // 채번 중복 확인

}
