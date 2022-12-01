package com.example.ebook.domain.cart.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long id;
    private Long productId;

    private String subject;
    private String description;
    private String writer;
    private int price;
}
