package com.example.demo.member.service;

import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.member.MemberMapper;
import com.example.demo.member.dto.MemberJoinRequest;
import com.example.demo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public long join(MemberJoinRequest req) throws Throwable {

        // 1. 비밀번호 확인 일치 검증
        if(!req.getPassword().equals(req.getPasswordConfirm())){
            throw new Throwable(String.valueOf(ErrorCode.PASSWORD_NOT_MATCHED));
        }

        // 2. 아이디 중복 검증
        if(memberMapper.countByUserId(req.getUserId()) > 0){ //dto로 받아온 아이디의 갯수가 이미 모델에서 0보다 큰 거면 존재중
            throw new Throwable(String.valueOf(ErrorCode.DUPLICATE_USER_ID));
        }

        // 3. 비밀번호 해싱 후 저장
        Member member = Member.builder()
                .userId(req.getUserId())
                .password(passwordEncoder.encode(req.getPassword()))
                .userName(req.getUserName())
                .email(req.getEmail())
                .build();

        memberMapper.insert(member);
        return member.getId();
    }

    @Transactional
    public boolean isDuplicatedUserId(String userId){
        return memberMapper.countByUserId(userId) > 0;
    }


}
