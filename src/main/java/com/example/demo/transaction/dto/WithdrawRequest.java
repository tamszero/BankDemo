package com.example.demo.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
public class WithdrawRequest {

    private Long accountId;

    @NotNull(message = "금액을 입력해주세요")
    private BigDecimal amount;

    @NotBlank(message = "계좌 비밀번호를 입력해주세요")
    private String password;

}
