package com.example.eBook.domain.member.controller;

import com.example.eBook.domain.member.dto.InfoModifyForm;
import com.example.eBook.domain.member.dto.LoginForm;
import com.example.eBook.domain.member.dto.SignupForm;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.validator.SignFormValidator;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.global.util.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final SignFormValidator signFormValidator;
    private final MemberService memberService;
    private final MailService mailService;

    @GetMapping("/member/join")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "member/new_member";
    }

    @PostMapping("/member/join")
    public String signup(@Validated @ModelAttribute SignupForm signupForm, BindingResult bindingResult) {

        signFormValidator.validate(signupForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "member/new_member";
        }

        try {
            Member member = memberService.save(signupForm);
            mailService.sendSingUpMail(member.getEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.rejectValue("username", "duplicated", "이미 등록된 아이디입니다.");
            return "member/new_member";
        }

        return "redirect:/member/login";
    }

    @GetMapping("/member/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("error", error);
        model.addAttribute("errorMsg", "로그인이 실패했습니다.");
        model.addAttribute("loginForm", new LoginForm());
        return "member/login_member";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/modify/{memberId}")
    public String showModifyForm(Model model, @PathVariable(value = "memberId") Long memberId) {

        InfoModifyForm infoModifyForm = memberService.getInfoById(memberId);
        model.addAttribute("infoModifyForm", infoModifyForm);
        return "member/modify_info_member";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/member/modify/{memberId}")
    public String modifyInfo(@Validated @ModelAttribute InfoModifyForm infoModifyForm,
                             @PathVariable(value = "memberId") Long memberId, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/modify_info_member";
        }

        memberService.modifyInfo(memberId, infoModifyForm);
        return "redirect:/";
    }
}
