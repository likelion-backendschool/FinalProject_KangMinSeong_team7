package com.example.eBook.domain.home.controller;

import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/")
    public String showCommonHome(Model model) {
        List<PostDto> postList = postService.findRecentTop100();

        model.addAttribute("postList", postList);
        return "home/home";
    }

    @PreAuthorize("isAuthenticated() and hasAnyAuthority('ADMIN')")
    @GetMapping("/adm/home/main")
    public String showAdminHome() {
        return "home/adminHome";
    }
}
