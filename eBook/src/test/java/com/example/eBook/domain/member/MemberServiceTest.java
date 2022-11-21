package com.example.eBook.domain.member;

import com.example.eBook.domain.member.dto.InfoModifyForm;
import com.example.eBook.domain.member.dto.PwdModifyForm;
import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.exception.MemberNotFoundException;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입_정상")
    void save() {

        // given
        SignupForm signupForm = SignupForm.builder()
                .username("test2_username")
                .password("1234")
                .passwordConfirm("1234")
                .nickname("test_nickname2")
                .email("test2@email.com")
                .build();

        // when
        Member member = memberService.save(signupForm);
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertThat(findMember.getUsername()).isEqualTo(member.getUsername()),
                () -> assertThat(findMember.getNickname()).isEqualTo(member.getNickname()),
                () -> assertThat(findMember.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(findMember.getAuthLevel()).isEqualTo(3L),
                () -> assertThat(findMember.getCreateDate()).isNotNull(),
                () -> assertThat(findMember.getUpdateDate()).isNotNull()
        );
    }

    @Test
    @DisplayName("회원가입_이미존재하는아이디")
    void saveFailed() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        SignupForm signupForm = SignupForm.builder()
                .username("test_username")
                .password("1234")
                .passwordConfirm("1234")
                .nickname("test_nickname2")
                .email("test2@email.com")
                .build();

        // when then
        assertThatThrownBy(() -> memberService.save(signupForm))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("회원정보 가져오기")
    void getInfoByUsername() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when
        InfoModifyForm infoModifyForm = memberService.getInfoByUsername("test_username");

        // then
        assertAll(
                () -> assertThat(infoModifyForm.getEmail()).isEqualTo("test@email.com"),
                () -> assertThat(infoModifyForm.getNickname()).isEqualTo("test_nickname")
        );
    }

    @Test
    @DisplayName("이메일로 엔티티 가져오기")
    void findByEmail() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        assertThat(memberService.findByEmail("test@email.com")).isNotNull();
    }

    @Test
    @DisplayName("이메일로 엔티티 가져오기 / 존재하지 않는 회원")
    void findNotExistEntityByEmail() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        assertThatThrownBy(() -> memberService.findByEmail("notExist@email.com"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("아이디로 엔티티 가져오기")
    void findByUsername() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        assertThat(memberService.findByUsername("test_username")).isNotNull();
    }

    @Test
    @DisplayName("아이디로 엔티티 가져오기 / 존재하지 않는 회원")
    void findNotExistEntityByUsername() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        assertThatThrownBy(() -> memberService.findByUsername("notExist_username"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("회원정보 변경")
    void modifyInfo() {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when
        InfoModifyForm infoModifyForm = new InfoModifyForm("modify@email.com", "modify_username");
        memberService.modifyInfo("test_username", infoModifyForm);

        // then
        Member findMember = memberRepository.findById(member.getId()).get();

        assertAll(
                () -> assertThat(findMember.getEmail()).isEqualTo(infoModifyForm.getEmail()),
                () -> assertThat(findMember.getNickname()).isEqualTo(infoModifyForm.getNickname())
        );
    }

    @Test
    @DisplayName("비밀번호 변경")
    void modifyPwd() {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PwdModifyForm pwdModifyForm = new PwdModifyForm("1234", "12345", "12345");

        // when
        memberService.modifyPwd("test_username", pwdModifyForm);

        // then
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(passwordEncoder.matches("12345", findMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("임시비밀번호 발급")
    void issueTemporaryPassword() {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when
        String temporaryPassword = memberService.IssueTemporaryPassword("test_username", "test@email.com");

        // then
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(passwordEncoder.matches(temporaryPassword, findMember.getPassword())).isTrue();
    }
}
