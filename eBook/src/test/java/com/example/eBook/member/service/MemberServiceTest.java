package com.example.eBook.member.service;

import com.example.eBook.domain.member.dto.InfoModifyForm;
import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void save() {
        SignupForm signupForm = SignupForm.builder()
                .username("test_username")
                .password("1234")
                .passwordConfirm("1234")
                .nickname("test_nickname")
                .email("test@email.com")
                .build();

        Member member = memberService.save(signupForm);

        Member findMember = memberRepository.findById(member.getId()).orElseThrow();
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getAuthLevel()).isEqualTo(3L);
        assertThat(findMember.getCreateDate()).isNotNull();
        assertThat(findMember.getUpdateDate()).isNotNull();
    }

    @Test
    @DisplayName("회원정보수정")
    public void modifyInfo() {
        Member member = Member.builder()
                .username("test_username")
                .password("1234")
                .email("test@email.com")
                .nickname("test_nickname")
                .authLevel(3L)
                .build();

        Member savedMember = memberRepository.save(member);
        memberService.modifyInfo(savedMember.getId(), new InfoModifyForm("1234@email.com", "12345"));

        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow();

        assertThat(findMember.getEmail()).isEqualTo("1234@email.com");
        assertThat(findMember.getNickname()).isEqualTo("12345");
    }
}