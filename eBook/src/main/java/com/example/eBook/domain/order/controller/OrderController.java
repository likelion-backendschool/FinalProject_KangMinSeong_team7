package com.example.eBook.domain.order.controller;

import com.example.eBook.domain.order.dto.OrderDto;
import com.example.eBook.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public String create(Principal principal) {

        orderService.save(principal.getName());
        return "redirect:/order/list";
    }

    @GetMapping("/list")
    public String showOrderList(Principal principal, Model model) {

        List<OrderDto> orderDtos = orderService.findAllByUsername(principal.getName());
        model.addAttribute("orderDtos", orderDtos);

        return "/order/list_order";
    }
}
