package com.example.demo.member;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.member.Member;
@Mapper
public interface MemberMapper {
    int insert(Member member);
    Member findByUserId(String userId);
    Member findById(Long id);
    int countByUserId(String userId); //아이디 중복 체크
}
