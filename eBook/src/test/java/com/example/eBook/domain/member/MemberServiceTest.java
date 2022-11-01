package com.example.eBook.domain.member;

import com.example.eBook.domain.member.dto.InfoModifyForm;
import com.example.eBook.domain.member.dto.PwdModifyForm;
import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.exception.MemberNotFoundException;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));
    }

    @Test
    @DisplayName("회원가입_정상")
    void save() {
        SignupForm signupForm = SignupForm.builder()
                .username("test2_username")
                .password("1234")
                .passwordConfirm("1234")
                .nickname("test_nickname2")
                .email("test2@email.com")
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
    @DisplayName("회원가입_이미존재하는아이디")
    void saveFailed() {
        SignupForm signupForm = SignupForm.builder()
                .username("test_username")
                .password("1234")
                .passwordConfirm("1234")
                .nickname("test_nickname2")
                .email("test2@email.com")
                .build();

        assertThatThrownBy(() -> memberService.save(signupForm))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("회원정보 가져오기")
    void getInfoByUsername() {
        InfoModifyForm infoModifyForm = memberService.getInfoByUsername("test_username");

        assertThat(infoModifyForm.getEmail()).isEqualTo("test@email.com");
        assertThat(infoModifyForm.getNickname()).isEqualTo("test_nickname");
    }

    @Test
    @DisplayName("이메일로 엔티티 가져오기")
    void findByEmail() {
        assertThat(memberService.findByEmail("test@email.com")).isNotNull();

        assertThatThrownBy(() -> memberService.findByEmail("notExist@email.com"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("아이디로 엔티티 가져오기")
    void findByUsername() {
        assertThat(memberService.findByUsername("test_username")).isNotNull();

        assertThatThrownBy(() -> memberService.findByUsername("notExist_username"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("회원정보 변경")
    void modifyInfo() {

        InfoModifyForm infoModifyForm = new InfoModifyForm("modify@email.com", "modify_username");
        memberService.modifyInfo("test_username", infoModifyForm);

        Member findMember = memberService.findByUsername("test_username");
        assertThat(findMember.getEmail()).isEqualTo(infoModifyForm.getEmail());
        assertThat(findMember.getNickname()).isEqualTo(infoModifyForm.getNickname());
    }

    @Test
    @DisplayName("비밀번호 변경")
    void modifyPwd() {
        PwdModifyForm pwdModifyForm = new PwdModifyForm("1234", "12345", "12345");

        memberService.modifyPwd("test_username", pwdModifyForm);
        Member findMember = memberService.findByUsername("test_username");

        assertThat(passwordEncoder.matches("12345", findMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("임시비밀번호 발급")
    void issueTemporaryPassword() {

        String temporaryPassword = memberService.IssueTemporaryPassword("test_username", "test@email.com");

        Member findMember = memberService.findByUsername("test_username");
        assertThat(passwordEncoder.matches(temporaryPassword, findMember.getPassword())).isTrue();
    }
}