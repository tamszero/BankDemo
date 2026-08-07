package com.example.demo.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //Member

    DUPLICATE_USER_ID("M001", "이미 사용중인 아이디입니다",HttpStatus.CONFLICT),
    PASSWORD_NOT_MATCHED("M002", "비밀번호가 일치하지 않습니다", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED("M003", "아이디 또는 비밀번호가 일치하지 않습니다", HttpStatus.UNAUTHORIZED),
    MEMBER_NOT_FOUND("M004", "존재하지 않는 회원입니다", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
