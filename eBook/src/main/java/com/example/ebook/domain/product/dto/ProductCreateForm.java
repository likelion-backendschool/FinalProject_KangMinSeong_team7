package com.example.ebook.domain.product.dto;

import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateForm {

    @NotEmpty(message = "상품명을 입력해주세요.")
    private String subject;

    private Integer price;

    @NotEmpty(message = "상품설명을 입력해주세요.")
    private String description;
}
