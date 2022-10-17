package com.example.eBook.domain.member.service;

import com.example.eBook.domain.member.dto.InfoModifyForm;
import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member save(SignupForm signupForm) {
        Member member = MemberMapper.INSTANCE.signupFormToEntity(signupForm);
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
    }

    @Transactional(readOnly = true)
    public InfoModifyForm getInfoById(Long memberId) {
        return MemberMapper.INSTANCE.EntityToInfoModifyForm(memberRepository.findById(memberId).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않는 회원입니다.")
        ));
    }

    public void modifyInfo(Long memberId, InfoModifyForm infoModifyForm) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        member.updateInfo(infoModifyForm.getEmail(), infoModifyForm.getNickname());
    }
}
