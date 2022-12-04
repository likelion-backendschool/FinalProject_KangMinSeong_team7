package com.example.ebook.domain.member.controller;

import com.example.ebook.domain.member.dto.*;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.exception.PasswordNotSameException;
import com.example.ebook.domain.member.validator.PwdModifyFormValidator;
import com.example.ebook.domain.member.validator.SignFormValidator;
import com.example.ebook.domain.member.service.MemberService;
import com.example.ebook.global.util.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;
    private final SignFormValidator signFormValidator;
    private final PwdModifyFormValidator pwdModifyFormValidator;

    private static final String REGISTER_VIEW = "member/new_member";
    private static final String REDIRECT_LOGIN_VIEW = "redirect:/member/login";
    private static final String MODIFY_PWD_VIEW = "member/modify_pwd_member";
    private static final String FIND_USERNAME_VIEW = "member/find_username_member";
    private static final String FIND_PWD_VIEW = "member/find_password_member";


    @PreAuthorize("isAnonymous()")
    @GetMapping("/member/join")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return REGISTER_VIEW;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/member/join")
    public String signup(@Validated @ModelAttribute SignupForm signupForm, BindingResult bindingResult) {

        signFormValidator.validate(signupForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return REGISTER_VIEW;
        }

        try {
            Member member = memberService.save(signupForm);
            mailService.sendSingUpMail(member.getEmail());
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("username", "duplicated", "이미 등록된 아이디입니다.");
            return REGISTER_VIEW;
        }

        return REDIRECT_LOGIN_VIEW;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/member/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {

        model.addAttribute("error", error);
        model.addAttribute("errorMsg", "로그인이 실패했습니다.");
        model.addAttribute("loginForm", new LoginForm());
        return "member/login_member";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/mypage")
    public String showMyPage(Model model, Principal principal) {

        InfoModifyForm userInfo = memberService.getInfoByUsername(principal.getName());
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("restCash", memberService.getRestCash(principal.getName()));

        return "member/mypage_member";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/modify")
    public String showModifyForm(Model model, Principal principal) {

        InfoModifyForm infoModifyForm = memberService.getInfoByUsername(principal.getName());
        model.addAttribute("infoModifyForm", infoModifyForm);
        return "member/modify_info_member";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/member/modify")
    public String modifyInfo(@Validated @ModelAttribute InfoModifyForm infoModifyForm, BindingResult bindingResult,
                             Principal principal) {

        if (bindingResult.hasErrors()) {
            return "member/modify_info_member";
        }

        memberService.modifyInfo(principal.getName(), infoModifyForm);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/modifyPassword")
    public String showModifyPasswordForm(Model model) {
        model.addAttribute("pwdModifyForm", new PwdModifyForm());
        return MODIFY_PWD_VIEW;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/member/modifyPassword")
    public String modifyPassword(@Validated @ModelAttribute PwdModifyForm pwdModifyForm, BindingResult bindingResult,
                                 Principal principal) {

        pwdModifyFormValidator.validate(pwdModifyForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return MODIFY_PWD_VIEW;
        }

        try {
            memberService.modifyPwd(principal.getName(), pwdModifyForm);
        } catch (PasswordNotSameException e) {
            bindingResult.rejectValue("oldPassword", "notSame", e.getMessage());
            return MODIFY_PWD_VIEW;
        }

        return "redirect:/";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/member/findUsername")
    public String showFindUsernameForm(Model model) {
        model.addAttribute("usernameFindForm", new UsernameFindForm());
        return FIND_USERNAME_VIEW;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/member/findUsername")
    public String findUsername(@Validated @ModelAttribute UsernameFindForm usernameFindForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return FIND_USERNAME_VIEW;
        }

        try {
            Member member = memberService.findByEmail(usernameFindForm.getEmail());
            mailService.sendUsername(member.getEmail(), member.getUsername());
        } catch (UsernameNotFoundException e) {
            bindingResult.rejectValue("email", "notFound", e.getMessage());
            return FIND_USERNAME_VIEW;
        }
        return REDIRECT_LOGIN_VIEW;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/member/findPassword")
    public String showFindPasswordForm(Model model) {
        model.addAttribute("passwordFindForm", new PasswordFindForm());
        return FIND_PWD_VIEW;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/member/findPassword")
    public String findPassword(@Validated @ModelAttribute PasswordFindForm passwordFindForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return FIND_PWD_VIEW;
        }

        try {
            String temporaryPassword = memberService.issueTemporaryPassword(passwordFindForm.getUsername(), passwordFindForm.getEmail());
            mailService.sendTemporaryPassword(passwordFindForm.getEmail(), temporaryPassword);
        } catch (UsernameNotFoundException e) {
            bindingResult.reject("notFound", e.getMessage());
            return FIND_PWD_VIEW;
        }

        return REDIRECT_LOGIN_VIEW;
    }
}
