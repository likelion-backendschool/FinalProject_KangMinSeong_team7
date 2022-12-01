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
    private List<PostKeyword> postKeywordList;
//    @NotEmpty(message = "키워드를 하나 선택해주세요.")
    private PostKeyword postKeyword;

    public ProductCreateForm(List<PostKeyword> postKeywordList) {
        this.postKeywordList = postKeywordList;
    }
}
