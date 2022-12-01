package com.example.ebook.domain.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RecentPostDto {

    private String subject;
    private String writer;
    private LocalDateTime createDate;
}
