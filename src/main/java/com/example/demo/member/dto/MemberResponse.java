package com.example.demo.member.dto;

import com.example.demo.account.Account;
import com.example.demo.account.dto.AccountResponse;
import com.example.demo.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String userId; //user_id
    private String userName; //user_name
    private String email;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getUserId(),
                member.getUserName(),
                member.getEmail()
        );

    }
}


