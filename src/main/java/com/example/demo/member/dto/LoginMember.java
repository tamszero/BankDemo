package com.example.demo.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//세션 보관
//비번 분리된 로그인 성공한 유저를 위한 dto
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginMember {

    private Long id;
    private String userId;
    private String userName;
}
