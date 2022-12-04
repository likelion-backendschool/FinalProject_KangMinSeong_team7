package com.example.ebook.domain.withdraw.controller;

import com.example.ebook.domain.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/withdraw")
public class AdmWithdrawController {

    private final WithdrawService withdrawService;

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @GetMapping("/applyList")
    public String showApplyList(Model model) {

        model.addAttribute("withdrawApplies", withdrawService.findAllAdmWithdrawApplyDto());
        return "withdraw/adm_list_withdraw";
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @PostMapping("/{withdrawApplyId}")
    public String withdraw(@PathVariable("withdrawApplyId") Long id) {

        withdrawService.disposeWithdraw(id);
        return "redirect:/adm/withdraw/applyList";
    }
}
