package com.example.eBook.domain.post.controller;

import com.example.eBook.domain.post.dto.PostDetailDto;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.dto.PostModifyForm;
import com.example.eBook.domain.post.dto.PostWriteForm;
import com.example.eBook.domain.post.service.PostService;
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
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public String showRecentPost(Model model) {
        List<PostDto> postList = postService.findRecentTop100();

        model.addAttribute("postList", postList);
        return "home/home";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/list")
    public String showPostList(Model model) {
        List<PostDto> postList = postService.findAll();
        model.addAttribute("postList", postList);

        return "post/list_post";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/write")
    public String showWriteForm(Model model) {
        model.addAttribute("postWriteForm", new PostWriteForm());
        return "post/write_post";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/write")
    public String save(@Validated @ModelAttribute PostWriteForm postWriteForm, BindingResult bindingResult,
                       Principal principal) {

        if (bindingResult.hasErrors()) {
            return "post/write_post";
        }

        postService.save(principal.getName(), postWriteForm);
        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/{postId}")
    public String showPostDetail(@PathVariable Long postId, Model model) {

        PostDetailDto postDetailDto = postService.getPostDetail(postId);
        model.addAttribute("postDetailDto", postDetailDto);
        return "post/detail_post";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/{postId}/modify")
    public String showModifyForm(@PathVariable Long postId, Model model) {

        PostModifyForm postModifyForm = postService.getPostModifyForm(postId);
        model.addAttribute("postModifyForm", postModifyForm);

        return "post/modify_post";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post/{postId}/modify")
    public String modify(@PathVariable Long postId, @Validated @ModelAttribute PostModifyForm postModifyForm,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "post/modify_post";
        }

        postService.modify(postId, postModifyForm);
        return "redirect:/post/%s".formatted(postId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post/{postId}/delete")
    public String delete(@PathVariable Long postId) {

        postService.delete(postId);
        return "redirect:/post/list";
    }
}
