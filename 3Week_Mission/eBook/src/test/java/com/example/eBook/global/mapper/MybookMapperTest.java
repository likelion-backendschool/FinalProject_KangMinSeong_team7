package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.mybook.dto.MybookDto;
import com.example.eBook.domain.mybook.entity.Mybook;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.product.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MybookMapperTest {

    @Test
    @DisplayName("entity_To_MybookDto_Mapper")
    void entityToMybookDto() {

        Mybook mybook = Mybook.builder()
                .id(1L)
                .member(new Member())
                .product(Product.builder().id(1L).subject("product_subject").description("product_description").postKeyword(
                        PostKeyword.builder().id(1L).content("postKey_content").build())
                        .price(1000)
                        .member(Member.builder().nickname("member_nickname").build())
                        .build())
                .build();

        MybookDto mybookDto = MybookMapper.INSTANCE.entityToMybookDto(mybook);

        assertThat(mybookDto.getId()).isEqualTo(mybook.getId());
        assertThat(mybookDto.getProductId()).isEqualTo(mybook.getProduct().getId());
        assertThat(mybookDto.getProductSubject()).isEqualTo(mybook.getProduct().getSubject());
        assertThat(mybookDto.getProductDescription()).isEqualTo(mybook.getProduct().getDescription());
        assertThat(mybookDto.getProductWriter()).isEqualTo(mybook.getProduct().getMember().getNickname());
        assertThat(mybookDto.getProductKeywordContent()).isEqualTo(mybook.getProduct().getPostKeyword().getContent());
    }
}
