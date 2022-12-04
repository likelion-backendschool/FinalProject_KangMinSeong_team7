package com.example.ebook.domain.product.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto {

    private Long id;
    private String subject;
    private Integer price;
    private String description;
    private String writer;
    private LocalDateTime createDate;
    private String postKeywordContent;
}
