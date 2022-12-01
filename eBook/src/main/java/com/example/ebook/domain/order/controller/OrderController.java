package com.example.ebook.domain.order.controller;

import com.example.ebook.domain.member.service.MemberService;
import com.example.ebook.domain.order.dto.OrderDetailDto;
import com.example.ebook.domain.order.dto.OrderDto;
import com.example.ebook.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    @PostMapping("/create")
    public String create(Principal principal) {

        Long orderId = orderService.save(principal.getName());
        return "redirect:/order/%s".formatted(orderId);
    }

    @GetMapping("/list")
    public String showOrderList(Principal principal, Model model) {

        List<OrderDto> orderDtos = orderService.findAllByUsername(principal.getName());
        model.addAttribute("orderDtos", orderDtos);

        return "/order/list_order";
    }

    @GetMapping("/{orderId}")
    public String showOrderDetail(@PathVariable("orderId") Long orderId, Principal principal, Model model) {

        OrderDetailDto orderDetailDto = orderService.getOrderDetail(orderId);
        int rashCash = memberService.getRestCash(principal.getName());

        model.addAttribute("orderDetailDto", orderDetailDto);
        model.addAttribute("restCash", rashCash);
        model.addAttribute("nowTime", LocalDateTime.now());
        return "order/detail_order";
    }
}
