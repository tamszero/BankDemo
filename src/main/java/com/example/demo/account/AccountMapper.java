package com.example.demo.account;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AccountMapper {
    int insert(Account account);
    List<Account> findByMemberId(Long memberId);
    Account findById(Long id);
    Account findByAccountNumber(String accountNumber); //수취인 계좌 찾기
    int countByAccountNumber(String accountNumber); // 채번 중복 확인

    Account findByIdForUpdate(Long id); //락 획득용
    int updateBalance(@Param("id") Long id, @Param("balance") BigDecimal balance);

}
