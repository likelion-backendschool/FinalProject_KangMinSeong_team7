package com.example.eBook.domain.post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostWithKeywordDto {

    private Long id;
    private String subject;
    private String writer;
    private List<String> postKeywordContents;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
