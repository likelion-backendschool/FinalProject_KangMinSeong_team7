package com.example.ebook.domain.withdraw.controller;

import com.example.ebook.domain.member.service.MemberService;
import com.example.ebook.domain.withdraw.dto.WithdrawApplyDto;
import com.example.ebook.domain.withdraw.dto.WithdrawApplyForm;
import com.example.ebook.domain.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/withdraw")
public class WithdrawController {

    private final MemberService memberService;
    private final WithdrawService withdrawService;

    @GetMapping("/apply")
    public String showApplyForm(Model model, Principal principal) {

        withdrawService.canApplyWithdraw(principal.getName());

        int restCash = memberService.getRestCash(principal.getName());

        model.addAttribute("restCash", restCash);
        model.addAttribute("withdrawApplyForm", new WithdrawApplyForm());
        return "withdraw/apply_withdraw";
    }

    @PostMapping("/apply")
    public String apply(@Validated @ModelAttribute WithdrawApplyForm withdrawApplyForm, BindingResult bindingResult,
                        Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("restCash", memberService.getRestCash(principal.getName()));
            return "withdraw/apply_withdraw";
        }

        withdrawService.apply(withdrawApplyForm, principal.getName());
        return "redirect:/member/mypage";
    }

    @GetMapping("history")
    public String showHistory(Model model, Principal principal) {
        List<WithdrawApplyDto> withdrawApplies = withdrawService.findAllByMember(principal.getName());
        model.addAttribute("withdrawApplies", withdrawApplies);

        return "withdraw/history_withdraw";
    }

    @GetMapping("/history/{withdrawApplyId}/delete")
    public String delete(@PathVariable("withdrawApplyId") Long id, Principal principal) {
        withdrawService.delete(id, principal.getName());

        return "redirect:/withdraw/history";
    }
}
