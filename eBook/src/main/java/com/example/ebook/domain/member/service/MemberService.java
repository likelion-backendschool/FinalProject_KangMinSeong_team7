package com.example.ebook.domain.member.service;

import com.example.ebook.domain.member.dto.reponse.MemberInfoResponse;
import com.example.ebook.domain.member.dto.reponse.MemberInfoResponseDto;
import com.example.ebook.domain.member.dto.request.LoginFormRequest;
import com.example.ebook.global.api.exception.member.ApiMemberNotFoundException;
import com.example.ebook.global.api.exception.member.LoginFailedException;
import com.example.ebook.domain.member.dto.InfoModifyForm;
import com.example.ebook.domain.member.dto.PwdModifyForm;
import com.example.ebook.domain.member.dto.SignupForm;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.exception.MemberNotFoundException;
import com.example.ebook.domain.member.exception.PasswordNotSameException;
import com.example.ebook.domain.member.repository.MemberRepository;
import com.example.ebook.global.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom random = new SecureRandom();

    public Member save(SignupForm signupForm) {
        Member member = MemberMapper.INSTANCE.signupFormToEntity(signupForm);
        member.updatePassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
    }

    @Transactional(readOnly = true)
    public InfoModifyForm getInfoByUsername(String username) {
        return MemberMapper.INSTANCE.entityToInfoModifyForm(memberRepository.findByUsername(username).orElseThrow(
                () -> new MemberNotFoundException("존재하지 않는 회원입니다.")
        ));
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
    }

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new MemberNotFoundException("존재하지 않는 회원입니다."));
    }

    public void modifyInfo(String username, InfoModifyForm infoModifyForm) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        member.updateInfo(infoModifyForm.getEmail(), infoModifyForm.getNickname());
    }

    public void modifyPwd(String username, PwdModifyForm pwdModifyForm) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(pwdModifyForm.getOldPassword(), member.getPassword())) {
            throw new PasswordNotSameException("기존 비밀번호와 일치하지 않습니다.");
        }

        member.updatePassword(passwordEncoder.encode(pwdModifyForm.getPassword()));
    }

    public String issueTemporaryPassword(String username, String email) {
        Member member = memberRepository.findByUsernameAndEmail(username, email).orElseThrow(
                () -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        String temporaryPassword = getTemporaryPassword();
        member.updatePassword(passwordEncoder.encode(temporaryPassword));
        return temporaryPassword;
    }

    @Transactional(readOnly = true)
    public int getRestCash(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        return member.getRestCash();
    }

    // REST API
    @Transactional(readOnly = true)
    public Member confirmLogin(LoginFormRequest loginFormRequest) {
        Member member = memberRepository.findByUsername(loginFormRequest.getUsername())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(loginFormRequest.getPassword(), member.getPassword())) {
            throw new LoginFailedException();
        }

        return member;
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getInfoByUsernameForApi(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(ApiMemberNotFoundException::new);

        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.toResponseDto(member);

        return new MemberInfoResponse(memberInfoResponseDto);
    }

    public String getTemporaryPassword() {
        String alphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int alphaNumLength = alphaNum.length();

        StringBuilder temporaryPassword = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            temporaryPassword.append(alphaNum.charAt(random.nextInt(alphaNumLength)));
        }

        return temporaryPassword.toString();
    }
}
