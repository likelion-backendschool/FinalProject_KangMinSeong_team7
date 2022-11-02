package com.example.eBook.domain.mybook.controller;

import com.example.eBook.domain.mybook.dto.MybookDto;
import com.example.eBook.domain.mybook.service.MybookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mybook")
public class MybookController {

    private final MybookService mybookService;

    @GetMapping("/list")
    public String showMybooks(Principal principal, Model model) {

        List<MybookDto> mybookDtos = mybookService.findAllBooksByUsername(principal.getName());
        model.addAttribute("mybookDtos", mybookDtos);

        return "mybook/list_mybook";
    }
}
