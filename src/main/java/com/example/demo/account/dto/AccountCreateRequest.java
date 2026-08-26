package com.example.demo.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Getter @Setter
@NoArgsConstructor
public class AccountCreateRequest {
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^[0-9]{4}$", message = "계좌 비밀번호는 숫자 4자리입니다")
    private String password;
    @NotBlank(message = "비밀번호를 확인해주세요")
    private String passwordConfirm;
}
