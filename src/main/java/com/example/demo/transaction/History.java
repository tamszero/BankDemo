package com.example.demo.transaction;

//도메인

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    private Long id;
    private String txType; // Deposit / Withdraw / Transfer
    private BigDecimal amount;
    private Long wAccountId; // 출금 계좌
    private Long dAccountId; // 입금 계좌
    private BigDecimal wBalance; // 출금 후 계좌
    private BigDecimal dBalance; // 입금 후 잔액
    private LocalDateTime createdAt;
}
