package com.example.eBook.domain.cart.controller;

import com.example.eBook.domain.cart.dto.CartItemDto;
import com.example.eBook.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cart/list")
    public String showCartList(Principal principal, Model model) {
        List<CartItemDto> cartItemDtoList = cartService.cartItemDtoConverter(principal.getName());
        model.addAttribute("cartItemDtoList", cartItemDtoList);

        return "cart/list_cart";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cart/add/{productId}")
    public String addCart(@PathVariable("productId") Long productId, Principal principal) {

        cartService.save(productId, principal.getName());

        return "redirect:/cart/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cart/remove/{productId}")
    public String removeCart(@PathVariable("productId") Long productId) {

        cartService.delete(productId);
        return "redirect:/cart/list";
    }
}
