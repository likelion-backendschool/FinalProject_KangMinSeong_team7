package com.example.eBook.domain.product.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String subject;
    private int price;
    private String writer;
    private LocalDateTime createDate;
}
