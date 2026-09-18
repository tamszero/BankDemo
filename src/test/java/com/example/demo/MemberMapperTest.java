package com.example.demo;

import com.example.demo.account.Account;
import com.example.demo.account.AccountMapper;
import com.example.demo.member.model.Member;
import com.example.demo.member.mapper.MemberMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
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

    @MockitoSpyBean
    AccountMapper accountMapper;
    @Test
    void status가_조회된다() {
        Account account = accountMapper.findById(1L);
        assertThat(account.getStatus()).isEqualTo("ACTIVE");   // null이면 SELECT 누락
    }
}