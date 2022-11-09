package com.example.eBook.domain.member.controller;

import com.example.eBook.domain.member.dto.reponse.LoginResponse;
import com.example.eBook.domain.member.dto.reponse.MemberInfoResponse;
import com.example.eBook.domain.member.dto.request.LoginFormRequest;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.global.api.reponse.dto.SuccessSingleResponse;
import com.example.eBook.global.api.reponse.service.ResponseService;
import com.example.eBook.global.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
    public SuccessSingleResponse<LoginResponse> login(@Validated @RequestBody LoginFormRequest loginFormRequest) {
        Member member = memberService.confirmLogin(loginFormRequest);

        return responseService.getSuccessSingleDataResponse(
                new LoginResponse(jwtTokenProvider.createToken(String.valueOf(member.getUsername()), member.getRoles())));
    }

    @GetMapping("/me")
    public SuccessSingleResponse<MemberInfoResponse> getMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return responseService.getSuccessSingleDataResponse(memberService.getInfoByUsernameForApi(username));
    }
}
