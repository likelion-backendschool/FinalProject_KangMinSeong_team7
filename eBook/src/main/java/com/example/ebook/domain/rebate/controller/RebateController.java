package com.example.ebook.domain.rebate.controller;

import com.example.ebook.domain.rebate.dto.MakeDataForm;
import com.example.ebook.domain.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
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

        rebateService.makeRebateData(makeDataForm);

        return "redirect:/adm/rebate/rebateOrderItemList?yearMonth=%s".formatted(makeDataForm.getYearMonth());
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @GetMapping("/rebateOrderItemList")
    public String showRebateItemList(
            @RequestParam(value = "yearMonth", required = false) String yearMonth, Model model) {

        if (!StringUtils.hasText(yearMonth)) {
            yearMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        model.addAttribute("rebateOrderItems", rebateService.findAllByYearAndMonth(yearMonth));
        model.addAttribute("yearMonth", yearMonth);

        return "rebate/rebateItemsList_rebase";
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @PostMapping("/rebateOne/{rebateOrderItemId}")
    public String rebateOne(@PathVariable("rebateOrderItemId") Long rebateOrderItemId, HttpServletRequest request) {
        rebateService.rebateOne(rebateOrderItemId);

        if (request.getHeader("Referer") != null) {
            return "redirect:%s".formatted(request.getHeader("Referer"));
        }

        return "redirect:/adm/rebate/rebateOrderItemList";
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @PostMapping("/rebate")
    public String rebateAll(@RequestParam("ids") String ids, HttpServletRequest request) {
        rebateService.rebateAll(ids);

        if (request.getHeader("Referer") != null) {
            return "redirect:%s".formatted(request.getHeader("Referer"));
        }

        return "redirect:/adm/rebate/rebateOrderItemList";
    }
}
