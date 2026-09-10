package com.example.demo.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//상대 계좌의 이체확인 페이지 전용

@Getter
@AllArgsConstructor
public class TransferTargetResponse {

    private String accountNumber;
    private String ownerName;
}
