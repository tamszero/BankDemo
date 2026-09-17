package com.example.demo.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString(exclude = {"currentPassword", "newPassword", "newPasswordConfirm"})
public class PasswordChangeRequest {

    @NotBlank(message = "현재 비밀번호를 입력해줏세요")
    private String currentPassword;

    @NotBlank @Size(min = 8, max = 20, message = "비밀번호는 8~20자입니다")
    private String newPassword;

    @NotBlank
    private String newPasswordConfirm;
}
