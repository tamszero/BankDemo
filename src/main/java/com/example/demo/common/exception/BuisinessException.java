package com.example.demo.common.exception;

import lombok.Getter;

@Getter
public class BuisinessException extends Throwable {

    private final ErrorCode errorCode;

    public BuisinessException(ErrorCode errorCode){
        super();
        this.errorCode = errorCode;
    }


}
