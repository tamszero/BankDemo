package com.example.demo.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.member.model.Member;
import org.apache.ibatis.annotations.Param;
import org.jspecify.annotations.Nullable;

@Mapper
public interface MemberMapper {
    int insert(Member member);
    Member findByUserId(String userId);
    Member findById(Long id);
    int countByUserId(String userId); //아이디 중복 체크

    //void updatePassword(Long memberId, @Nullable String encode);
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}
