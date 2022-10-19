package com.example.eBook.domain.product.dto;

import com.example.eBook.domain.postKeyword.entity.PostKeyword;
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
    private PostKeyword postKeyword;
}
