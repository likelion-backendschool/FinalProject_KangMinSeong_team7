package com.example.eBook.domain.order.controller;

import com.example.eBook.domain.order.service.OrderService;
import com.example.eBook.domain.order.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class PayController {

    private final OrderService orderService;
    private final PayService payService;

    @PostMapping("/{orderId}/pay-only-cash")
    public String payByOnlyRestCash(@PathVariable("orderId") Long orderId, Principal principal) {

        orderService.orderByRestCash(principal.getName(), orderId);
        return "redirect:/order/%s".formatted(orderId);
    }

    @RequestMapping("/{id}/success")
    public String successPayment(
            @PathVariable("id") Long id,
            @RequestParam Map<String, String> params,
            Model model, Principal principal) {

        if (payService.confirmPayments(params, id, principal.getName())) {
            return "redirect:/order/%s".formatted(id);
        }
        else {
            return "order/fail_order";
        }
    }
}
