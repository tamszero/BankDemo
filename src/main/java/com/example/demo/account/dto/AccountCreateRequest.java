package com.example.demo.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Getter @Setter
@NotBlank
@Pattern(regexp = "^[0-9]{4}$", message = "계좌 비밀번호는 숫자 4자리입니다")
public class AccountCreateRequest {
    private String password;

    @NotBlank
    private String passwordConfirm;
}
