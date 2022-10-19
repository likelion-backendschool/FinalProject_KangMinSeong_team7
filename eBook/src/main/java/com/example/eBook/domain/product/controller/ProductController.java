package com.example.eBook.domain.product.controller;

import com.example.eBook.domain.product.dto.ProductDto;
import com.example.eBook.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/list")
    public String showProductList(Model model) {

        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("productDtoList", productDtoList);
        return "product/list_product";
    }
}
