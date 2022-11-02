package com.example.eBook.domain.member.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordFindForm {

    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;
    @Email
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
}
