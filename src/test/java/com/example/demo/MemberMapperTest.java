package com.example.demo;

import com.example.demo.member.model.Member;
import com.example.demo.member.MemberMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberMapperTest {

    @Autowired
    MemberMapper memberMapper;

    @Test
    void 조회_및_저장() {
        // 샘플 데이터 조회
        Member gildong = memberMapper.findByUserId("gildong");
        assertThat(gildong).isNotNull();
        assertThat(gildong.getUserName()).isEqualTo("홍길동");
        assertThat(gildong.getCreatedAt()).isNotNull();   // 언더스코어 매핑 확인

        // 저장 후 PK 채워지는지
        Member newbie = Member.builder()
                .userId("newbie").password("hash")
                .userName("신규").email("new@test.com")
                .build();
        memberMapper.insert(newbie);
        assertThat(newbie.getId()).isNotNull();

        assertThat(memberMapper.countByUserId("newbie")).isEqualTo(1);
        assertThat(memberMapper.countByUserId("nobody")).isZero();
    }
}