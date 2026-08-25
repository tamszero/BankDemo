package com.example.demo.account.dto;

import com.example.demo.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;

    public static AccountResponse from(Account account) {
         return new AccountResponse(
                 account.getId(),
                 account.getAccountNumber(),
                 account.getBalance(),
                 account.getCreatedAt()
         );
    }
}
