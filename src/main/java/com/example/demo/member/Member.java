package com.example.demo.member;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password") //로그에 패스워드 해시 찍힘 방지
public class Member {

    private Long id;
    private String userId; //user_id
    private String password; //BCrypt 해시
    private String userName; //user_name
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
