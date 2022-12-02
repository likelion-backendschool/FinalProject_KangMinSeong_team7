package com.example.ebook.domain.member;

import com.example.ebook.domain.member.controller.MemberController;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.exception.MemberNotFoundException;
import com.example.ebook.domain.member.repository.MemberRepository;
import com.example.ebook.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입폼_보여주기")
    @WithAnonymousUser
    void showSignupForm() throws Exception {

        // given then
        mockMvc.perform(get("/member/join")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("member/new_member"))
                .andExpect(model().attributeExists("signupForm"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    @WithAnonymousUser
    void signup() throws Exception {

        // given then
        mockMvc.perform(post("/member/join")
                        .param("username", "user1")
                        .param("password", "1234")
                        .param("passwordConfirm", "1234")
                        .param("email", "email@naver.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("signup"))
                .andExpect(redirectedUrl("/member/login"));

        assertThat(memberService.findByUsername("user1")).isNotNull();
    }

    @Test
    @DisplayName("회원가입실패_비밀번호불일치")
    @WithAnonymousUser
    void signup2() throws Exception {

        // given then
        mockMvc.perform(post("/member/join")
                        .param("username", "user1")
                        .param("password", "1234")
                        .param("passwordConfirm", "12345")
                        .param("email", "email@naver.com")
                        .with(csrf()))
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("signup"));

        assertThatThrownBy(() -> memberService.findByUsername("user1"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("로그인폼_보여주기")
    @WithAnonymousUser
    void showLoginForm() throws Exception {

        // given then
        mockMvc.perform(get("/member/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("member/login_member"))
                .andExpect(model().attributeExists("loginForm"))
                .andDo(print());
    }

    @Test
    @DisplayName("정보수정폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showModifyForm() throws Exception {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        mockMvc.perform(get("/member/modify"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("member/modify_info_member"))
                .andExpect(model().attributeExists("infoModifyForm"))
                .andDo(print());
    }

    @Test
    @DisplayName("정보수정")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void modifyInfo() throws Exception {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        mockMvc.perform(post("/member/modify")
                        .param("email", "email2@naver.com")
                        .param("nickname", "nickname2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("modifyInfo"))
                .andExpect(redirectedUrl("/"));

        Member testMember = memberService.findByUsername("test_username");
        assertThat(testMember.getEmail()).isEqualTo("email2@naver.com");
        assertThat(testMember.getNickname()).isEqualTo("nickname2");
    }

    @Test
    @DisplayName("비밀번호수정폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showModifyPasswordForm() throws Exception {

        // when then
        mockMvc.perform(get("/member/modifyPassword"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("member/modify_pwd_member"))
                .andExpect(model().attributeExists("pwdModifyForm"))
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호수정")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void modifyPassword() throws Exception {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        mockMvc.perform(post("/member/modifyPassword")
                        .param("oldPassword", "1234")
                        .param("password", "11223344")
                        .param("passwordConfirm", "11223344")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("modifyPassword"))
                .andExpect(redirectedUrl("/"));

        Member testMember = memberService.findByUsername("test_username");
        assertThat(passwordEncoder.matches("11223344", testMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("아이디찾기폼_보여주기")
    @WithAnonymousUser
    void showFindUsernameForm() throws Exception {

        // when then
        mockMvc.perform(get("/member/findUsername"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("member/find_username_member"))
                .andExpect(model().attributeExists("usernameFindForm"))
                .andDo(print());
    }

    @Test
    @DisplayName("아이디찾기")
    @WithAnonymousUser
    void findUsername() throws Exception {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        mockMvc.perform(post("/member/findUsername")
                        .param("email", "test@email.com")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findUsername"))
                .andExpect(redirectedUrl("/member/login"));
    }

    @Test
    @DisplayName("비밀번호찾기폼_보여주기")
    @WithAnonymousUser
    void showFindPasswordForm() throws Exception {

        // when then
        mockMvc.perform(get("/member/findPassword"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("member/find_password_member"))
                .andExpect(model().attributeExists("passwordFindForm"))
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호찾기")
    @WithAnonymousUser
    void findPassword() throws Exception {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        mockMvc.perform(post("/member/findPassword")
                        .param("username", "test_username")
                        .param("email", "test@email.com")
                        .with(csrf()))

                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findPassword"))
                .andExpect(redirectedUrl("/member/login"));

        Member testMember = memberService.findByUsername("test_username");
        assertThat(passwordEncoder.matches("1234", testMember.getPassword())).isFalse();
    }
}


