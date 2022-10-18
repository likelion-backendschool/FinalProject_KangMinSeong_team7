package com.example.eBook.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String subject;
    private String writer;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
