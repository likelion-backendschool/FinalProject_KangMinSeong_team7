package com.example.eBook.domain.product.dto.response;

import com.example.eBook.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Long authorId;
    private String authorName;
    private String subject;

    public static ProductResponseDto toResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .createDate(product.getCreateDate())
                .modifyDate(product.getUpdateDate())
                .authorId(product.getMember().getId())
                .authorName(product.getMember().getNickname())
                .subject(product.getSubject())
                .build();
    }
}
