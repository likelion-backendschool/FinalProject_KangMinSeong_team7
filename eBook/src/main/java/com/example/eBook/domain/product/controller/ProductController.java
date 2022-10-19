package com.example.eBook.domain.product.controller;

import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.service.PostKeywordService;
import com.example.eBook.domain.product.dto.ProductCreateForm;
import com.example.eBook.domain.product.dto.ProductDto;
import com.example.eBook.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final PostKeywordService postKeywordService;

    @GetMapping("/product/list")
    public String showProductList(Model model) {

        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("productDtoList", productDtoList);
        return "product/list_product";
    }

    @GetMapping("/product/create")
    public String showCreateForm(Model model) {

        List<PostKeyword> postKeywords = postKeywordService.findAll();
        model.addAttribute("productCreateForm", new ProductCreateForm(postKeywords));
        return "product/create_product";
    }

    @PostMapping("/product/create")
    public String create(@Validated @ModelAttribute ProductCreateForm productCreateForm, BindingResult bindingResult,
                         Principal principal) {

        if (bindingResult.hasErrors()) {
            return "product/create_product";
        }

        productService.save(principal.getName(), productCreateForm);
        return "redirect:/product/list";
    }
}
