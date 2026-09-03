package com.example.demo.common.exception;

//컨트롤러에서 못 잡은 BuisinessException을 여기서 처리
// ->  500 대신 제대로 된 상태코드/메시지를 받기 위해

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BuisinessException.class)
    public ResponseEntity<String> handleBuisinessException(BuisinessException e){
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(errorCode.getMessage());
    }
}
