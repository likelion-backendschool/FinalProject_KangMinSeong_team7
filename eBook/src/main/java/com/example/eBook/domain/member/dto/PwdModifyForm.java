package com.example.eBook.domain.member.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PwdModifyForm {

    @NotEmpty(message = "기존 비빌번호를 입력해주세요.")
    private String oldPassword;
    @NotEmpty(message = "새로운 비빌번호를 입력해주세요.")
    private String password;
    @NotEmpty(message = "새로운 비빌번호 확인을 입력해주세요.")
    private String passwordConfirm;
}
