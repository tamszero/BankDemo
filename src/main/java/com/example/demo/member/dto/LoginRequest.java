package com.example.demo.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//로그인 폼
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private long id;

    @NotBlank(message = "아이디를 입력해주세요")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

}
