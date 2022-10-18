package com.example.eBook.domain.post.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostWriteForm {

    @NotEmpty(message = "글의 제목을 입력해주세요.")
    private String subject;
    @NotEmpty(message = "글의 내용을 입력해주세요.")
    private String content;
    private String keywords;
}
