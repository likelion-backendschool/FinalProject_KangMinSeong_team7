package com.example.eBook.domain.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String subject;
    private String writer;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
