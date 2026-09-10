package com.example.demo.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class TransferRequest {

    @NotNull(message = "출금 계좌를 선택해주세요")
    private Long fromAccountId;

    @NotBlank(message = "받는 분의 계좌번호를 입력해주세요")
    @Pattern(regexp = "^[0-9]{0,20}$", message = "계좌번호는 숫자 4~20자리입니다")
    private String toAccountNumber;

    @NotNull(message = "금액을 입력해주세요")
    @DecimalMin(value = "1", message = "1원 이상 입력해주세요")
    private BigDecimal amount;

    @NotBlank(message = "계좌 비밀번호를 입력해주세요")
    private String password;



}
