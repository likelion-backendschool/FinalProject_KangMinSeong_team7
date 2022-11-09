package com.example.eBook.domain.mybook.controller;

import com.example.eBook.domain.mybook.dto.response.MybooksResponse;
import com.example.eBook.domain.mybook.service.MybookService;
import com.example.eBook.global.api.reponse.dto.SuccessSingleResponse;
import com.example.eBook.global.api.reponse.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiMybookController {

    private final ResponseService responseService;
    private final MybookService mybookService;

    @GetMapping("/myBooks")
    public SuccessSingleResponse<MybooksResponse> getMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return responseService.getSuccessSingleDataResponse(mybookService.getMybooks(username));
    }
}
