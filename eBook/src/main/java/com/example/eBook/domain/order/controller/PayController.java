package com.example.eBook.domain.order.controller;

import com.example.eBook.domain.order.service.OrderService;
import com.example.eBook.domain.order.service.PayService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<JsonNode> responseEntity = payService.confirmPayments(params, id, principal.getName());
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return "redirect:/order/%s".formatted(id);
        } else {
            model.addAttribute("message", responseEntity.getBody().get("message").asText());
            model.addAttribute("code", responseEntity.getBody().get("code").asText());
            model.addAttribute("orderId", params.get("orderId"));
            return "order/fail_order";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(
            @PathVariable("id") Long id,
            @RequestParam String message,
            @RequestParam String code,
            @RequestParam String orderId, Model model) {

        model.addAttribute("message", message);
        model.addAttribute("code", code);
        model.addAttribute("orderId", orderId);
        return "order/fail_order";
    }


}
