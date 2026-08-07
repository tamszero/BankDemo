package com.example.demo.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

//Data 어노테이션은 getter,setter,tostring 등을 자동생성
//@Data
@Getter @Setter
@ToString(exclude = {"password", "passwordConfirm"})
@NoArgsConstructor
public class MemberJoinRequest {

    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 영문·숫자 4~20자입니다")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자입니다")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String passwordConfirm;

    @NotBlank(message = "이름을 입력해주세요")
    private String userName;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
}
