package com.example.demo.account.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
@NoArgsConstructor
public class HistoryView {
    private Long id;
    private String txType;
    private BigDecimal amount;
    private Long withdrawAccountId;
    private String withdrawAccountNumber;
    private String withdrawAccountHolder;
    private Long depositAccountId;
    private String depositAccountNumber;
    private String depositAccountHolder;
    private BigDecimal withdrawBalance;
    private BigDecimal depositBalance;
    private LocalDateTime createdAt;

    public String getCreatedAtText() {
        return createdAt == null ? "" : createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
