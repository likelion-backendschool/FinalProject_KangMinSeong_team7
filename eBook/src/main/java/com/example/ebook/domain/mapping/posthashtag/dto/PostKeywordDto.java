package com.example.ebook.domain.mapping.posthashtag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostKeywordDto {

    private Long id;
    private String content;
}
