package com.example.eBook.domain.post.controller;

import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public String showRecentPost() {
        return "home/home";
    }

    @GetMapping("/post/list")
    public String showPostList(Model model) {
        List<PostDto> postList = postService.findAll();
        model.addAttribute("postList", postList);

        return "post/list_post";
    }
}
