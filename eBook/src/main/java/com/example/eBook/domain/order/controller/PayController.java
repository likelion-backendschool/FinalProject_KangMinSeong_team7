package com.example.eBook.domain.order.controller;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class PayController {

    private final OrderService orderService;
    private final MemberService memberService;

    @PostMapping("/{orderId}/pay-only-cash")
    public String payByOnlyRestCash(@PathVariable("orderId") Long orderId, Principal principal) {

        orderService.orderByRestCash(principal.getName(), orderId);
        return "redirect:/order/%s".formatted(orderId);
    }
}
