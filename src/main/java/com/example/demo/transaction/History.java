package com.example.demo.transaction;

//도메인

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    private Long id;
    private String txType; // Deposit / Withdraw / Transfer
    private BigDecimal amount;
    private Long withdrawAccountId; // 출금 계좌
    private Long depositAccountId; // 입금 계좌
    private BigDecimal withdrawBalance; // 출금 후 계좌
    private BigDecimal depositBalance; // 입금 후 잔액
    private LocalDateTime createdAt;

    public String getCreatedAtText() {
        return createdAt == null ? "" : createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
