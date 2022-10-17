package com.example.eBook.domain.member.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoModifyForm {
    @Email
    private String email;
    private String nickname;
}
