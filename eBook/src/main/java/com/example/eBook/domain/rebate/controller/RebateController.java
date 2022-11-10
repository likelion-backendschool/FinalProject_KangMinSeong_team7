package com.example.eBook.domain.rebate.controller;

import com.example.eBook.domain.rebate.dto.MakeDataForm;
import com.example.eBook.domain.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.time.LocalDateTime;

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

        String[] yearAndMonth = makeDataForm.getYearMonth().split("-");
        return "redirect:/adm/rebate/rebateOrderItemList?year=%s&month=%s".formatted(yearAndMonth[0], yearAndMonth[1]);
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
