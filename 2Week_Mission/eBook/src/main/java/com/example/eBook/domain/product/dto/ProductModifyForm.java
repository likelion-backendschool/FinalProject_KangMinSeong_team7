package com.example.eBook.domain.product.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductModifyForm {

    @NotEmpty(message = "상품명을 입력해주세요.")
    private String subject;
    @NotEmpty(message = "상품설명을 입력해주세요.")
    private String description;
    private int price;
}
