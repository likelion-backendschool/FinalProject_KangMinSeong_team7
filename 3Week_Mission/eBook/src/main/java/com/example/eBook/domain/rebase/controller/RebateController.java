package com.example.eBook.domain.rebase.controller;

import com.example.eBook.domain.rebase.dto.MakeDataForm;
import com.example.eBook.domain.rebase.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
public class RebateController {

    private final RebateService rebateService;

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @GetMapping("/makeData")
    public String showMakeDataForm(Model model) {
        model.addAttribute("makeDataForm", new MakeDataForm());
        return "rebate/makeData_rebase";
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @PostMapping("/makeData")
    public String makeData(@Validated @ModelAttribute MakeDataForm makeDataForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "rebate/makeData_rebase";
        }

        rebateService.save(makeDataForm);
        return "redirect:/adm/rebate/rebateOrderItemList?year=%d&month=%d".formatted(year, month);
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @GetMapping("/rebateOrderItemList")
    public String showRebateItemList(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            Model model) {

        if (year == null && month == null) {
            model.addAttribute("rebateOrderItems", rebateService.findAll());
        } else if (month == null) {
            model.addAttribute("rebateOrderItems", rebateService.findAllByYear(year));
        } else if (year == null) {
            model.addAttribute("rebateOrderItems", rebateService.findAllByYearAndMonth(LocalDateTime.now().getYear(), month));
        } else {
            model.addAttribute("rebateOrderItems", rebateService.findAllByYearAndMonth(year, month));
        }

        return "rebate/rebateItemsList_rebase";
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @PostMapping("/rebateOne/{rebateOrderItemId}")
    public String rebateOne(@PathVariable("rebateOrderItemId") Long rebateOrderItemId) {
        rebateService.rebateOne(rebateOrderItemId);

        return "redirect:/adm/rebate/rebateOrderItemList";
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @PostMapping("/rebate")
    public String rebateAll(@RequestParam("ids") String ids) {
        rebateService.rebateAll(ids);

        return "redirect:/adm/rebate/rebateOrderItemList";
    }
}
