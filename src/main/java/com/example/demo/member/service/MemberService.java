package com.example.demo.member.service;

import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.member.dto.MemberResponse;
import com.example.demo.member.dto.PasswordChangeRequest;
import com.example.demo.member.mapper.MemberMapper;
import com.example.demo.member.dto.JoinRequest;
import com.example.demo.member.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public long join(JoinRequest req) throws BuisinessException {

        // 1. 비밀번호 확인 일치 검증
        if(!req.getPassword().equals(req.getPasswordConfirm())){
            throw new BuisinessException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        // 2. 아이디 중복 검증
        if(memberMapper.countByUserId(req.getUserId()) > 0){ //dto로 받아온 아이디의 갯수가 이미 모델에서 0보다 큰 거면 존재중
            throw new BuisinessException(ErrorCode.DUPLICATE_USER_ID);
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
    public void changePassword(PasswordChangeRequest req, Long memberId) throws BuisinessException {

        Member member = memberMapper.findById(memberId);

        if(member == null){
            throw new BuisinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 현재 비밀번호 확인
        if(!passwordEncoder.matches(req.getCurrentPassword(), member.getPassword())){
            throw new BuisinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }
        // 새 비밀번호 일치 확인
        if(!req.getNewPassword().equals(req.getNewPasswordConfirm())){
            throw new BuisinessException(ErrorCode.PASSWORD_NOT_MATCHED);
        }
        // 현재와 같은 비밀번호 거부
        if(passwordEncoder.matches(req.getNewPassword(), member.getPassword())){
           throw new BuisinessException(ErrorCode.SAME_AS_CURRENT_PASSWORD);
        }

        memberMapper.updatePassword(memberId, passwordEncoder.encode(req.getNewPassword()));
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) throws BuisinessException{
        Member member = memberMapper.findById(id);
        if(member == null){
            throw new BuisinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

        return MemberResponse.from(member);

    }



    /**
    @Transactional
    public boolean isDuplicatedUserId(String userId){
        return memberMapper.countByUserId(userId) > 0;
    }
    */


}
