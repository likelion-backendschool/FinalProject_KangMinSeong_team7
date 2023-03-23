package com.example.ebook.domain.product.controller;

import com.example.ebook.domain.product.dto.ProductCreateForm;
import com.example.ebook.domain.product.dto.ProductDetailDto;
import com.example.ebook.domain.product.dto.ProductDto;
import com.example.ebook.domain.product.dto.ProductModifyForm;
import com.example.ebook.domain.product.service.ProductService;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product/list")
    public String showProductList(Model model) {

        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("productDtoList", productDtoList);
        return "product/list_product";
    }

    @PreAuthorize("hasAnyRole(WRITER)")
    @GetMapping("/product/create")
    public String showCreateForm(Model model) {
        model.addAttribute("productCreateForm", new ProductCreateForm());
        return "product/create_product";
    }

    @PreAuthorize("hasAnyRole(WRITER)")
    @PostMapping("/product/create")
    public String create(@Validated @ModelAttribute ProductCreateForm productCreateForm, BindingResult bindingResult,
                         Principal principal) {

        if (bindingResult.hasErrors()) {
            return "product/create_product";
        }

        productService.save(principal.getName(), productCreateForm);
        return "redirect:/product/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product/{productId}")
    public String showProductDetail(@PathVariable Long productId, Model model) {
        ProductDetailDto productDetailDto = productService.getProductDetail(productId);
        model.addAttribute("productDetailDto", productDetailDto);

        return "product/detail_product";
    }

    @PreAuthorize("hasAnyRole(WRITER)")
    @GetMapping("/product/{productId}/modify")
    public String showModifyForm(@PathVariable Long productId, Model model) {
        ProductModifyForm productModifyForm = productService.getProductModifyForm(productId);

        model.addAttribute("productModifyForm", productModifyForm);
        return "product/modify_product";
    }

    @PreAuthorize("hasAnyRole(WRITER)")
    @PostMapping("/product/{productId}/modify")
    public String modify(@PathVariable Long productId, @Validated @ModelAttribute ProductModifyForm productModifyForm,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "product/modify_product";
        }

        productService.modify(productId, productModifyForm);

        return "redirect:/product/%s".formatted(productId);
    }

    @PreAuthorize("hasAnyRole(WRITER)")
    @GetMapping("/product/{productId}/delete")
    public String delete(@PathVariable Long productId) {
        productService.delete(productId);

        return "redirect:/product/list";
    }
}
