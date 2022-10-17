package com.example.eBook.domain.member.service;

import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.member.exception.MemberNotFoundException;
import com.example.eBook.global.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member save(SignupForm signupForm) {
        Member member = MemberMapper.INSTANCE.signupFormToEntity(signupForm);
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("해당 멤버는 존재하지 않습니다."));
    }
}
