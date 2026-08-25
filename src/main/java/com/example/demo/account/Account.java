package com.example.demo.account;

//도메인

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")

public class Account {
    private Long id;
    private String accountNumber; //계좌번호로 연산을 따로 하는 작업이 필요없기땜에 스트링으로 해도 됨
    private  String password; //BCrypt 해시
    private BigDecimal balance;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
