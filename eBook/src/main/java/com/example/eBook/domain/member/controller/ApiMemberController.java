package com.example.eBook.domain.member.controller;

import com.example.eBook.domain.member.dto.reponse.LoginRequest;
import com.example.eBook.domain.member.dto.request.LoginFormRequest;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.global.api.reponse.dto.SuccessSingleResponse;
import com.example.eBook.global.api.reponse.service.ResponseService;
import com.example.eBook.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class ApiMemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;

    @PostMapping("/login")
    public SuccessSingleResponse<LoginRequest> login(@Validated @RequestBody LoginFormRequest loginFormRequest) {
        Member member = memberService.confirmLogin(loginFormRequest);

        return responseService.getSuccessSingleDataResponse(
                new LoginRequest(jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles())));
    }
}
