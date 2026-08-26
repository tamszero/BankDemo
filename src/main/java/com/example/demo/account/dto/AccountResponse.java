package com.example.demo.account.dto;

import com.example.demo.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    public String getCreatedAtText() {
        return createdAt == null ? "" :
                createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
