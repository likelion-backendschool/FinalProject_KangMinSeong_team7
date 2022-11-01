package com.example.eBook.domain.member.service;

import com.example.eBook.domain.member.dto.InfoModifyForm;
import com.example.eBook.domain.member.dto.PwdModifyForm;
import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.exception.MemberNotFoundException;
import com.example.eBook.domain.member.exception.PasswordNotSameException;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.global.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final static Random random = new Random();

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
        return MemberMapper.INSTANCE.EntityToInfoModifyForm(memberRepository.findByUsername(username).orElseThrow(
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

    public String IssueTemporaryPassword(String username, String email) {
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
