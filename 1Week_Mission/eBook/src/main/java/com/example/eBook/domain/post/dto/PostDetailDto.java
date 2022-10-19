package com.example.eBook.domain.post.dto;

import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDto {
    private Long id;
    private String subject;
    private String writer;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String content;

    private List<PostKeyword> postKeywords;
}
