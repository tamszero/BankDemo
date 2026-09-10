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
    MEMBER_NOT_FOUND("M004", "존재하지 않는 회원입니다", HttpStatus.NOT_FOUND),

    ACCOUNT_NOT_FOUNT("A001", "존재하지 않는 계좌입니다", HttpStatus.NOT_FOUND),
    NOT_ACCOUNT_OWNER("A002", "본인 계좌가 아닙니다", HttpStatus.FORBIDDEN),
    INVALID_ACCOUNT_PASSWORD("A003", "계좌 비밀번호가 일치하지 않습니다", HttpStatus.BAD_REQUEST),
    ACCOUNT_NUMBER_GENERATION_FAILED("A004", "계좌번호 생성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    INSUFFICIENT_BALANCE("T001", "잔액이 부족합니다", HttpStatus.BAD_REQUEST),
    INVALID_AMOUNT("T002", "거래 금액은 0보다 커야 합니다", HttpStatus.BAD_REQUEST),
    SELF_TRANSFER_NOT_ALLOWED("T003","자기 자신에게 송금할 수 없습니다.", HttpStatus.BAD_REQUEST ),
    DUPLICATE_REQUEST("T004", "반복된 요청입니다", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
