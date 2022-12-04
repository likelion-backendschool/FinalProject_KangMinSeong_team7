package com.example.ebook.domain.member.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoModifyForm {
    @Email
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    private String nickname;
}
