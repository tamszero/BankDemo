package com.example.demo.transaction.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class DepositRequest {

    @NotNull(message = "계좌를 입력해주세요")
    private Long accountId;

    @NotNull(message = "금액을 입력해주세요")
    @DecimalMin(value = "1", message = "1원 이상 입력해주세요")
    private BigDecimal amount;

    @NotBlank(message = "계좌 비밀번호를 입력해주세요")
    private String password;
}
