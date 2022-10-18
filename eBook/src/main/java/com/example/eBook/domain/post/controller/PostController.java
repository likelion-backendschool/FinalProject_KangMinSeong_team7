package com.example.eBook.domain.post.controller;

import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.dto.PostWriteForm;
import com.example.eBook.domain.post.service.PostService;
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

    @GetMapping("/post/write")
    public String showWriteForm(Model model) {
        model.addAttribute("postWriteForm", new PostWriteForm());
        return "post/write_post";
    }

    @PostMapping("/post/write")
    public String save(@Validated @ModelAttribute PostWriteForm postWriteForm, BindingResult bindingResult,
                       Principal principal) {

        if (bindingResult.hasErrors()) {
            return "post/write_post";
        }

        postService.save(principal.getName(), postWriteForm);
        return "redirect:/post/list";
    }
}
