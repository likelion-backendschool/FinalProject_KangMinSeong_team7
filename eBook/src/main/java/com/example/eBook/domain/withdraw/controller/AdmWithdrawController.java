package com.example.eBook.domain.withdraw.controller;

import com.example.eBook.domain.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}
