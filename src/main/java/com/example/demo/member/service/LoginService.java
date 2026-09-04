package com.example.demo.member.service;


import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.member.mapper.MemberMapper;
import com.example.demo.member.dto.LoginMember;
import com.example.demo.member.dto.LoginRequest;
import com.example.demo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public LoginMember login(LoginRequest req) throws BuisinessException {

        //아이디 받아옴
        Member member = memberMapper.findByUserId(req.getUserId());

        //입력받은 비밀번호와 모델 비밀번호가 일치하는지 확인
        //아이디 존재하는지 확인은 공격 대상이 될 수 있어서 하지말래...
        if(member == null || !passwordEncoder.matches(req.getPassword(), member.getPassword())){
            throw new BuisinessException(ErrorCode.LOGIN_FAILED);
        }

        //로그인dto에 리턴
        return new LoginMember(member.getId(), member.getUserId(), member.getUserName());
    }
}
