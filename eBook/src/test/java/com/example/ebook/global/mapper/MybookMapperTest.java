package com.example.ebook.global.mapper;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.mybook.dto.MybookDto;
import com.example.ebook.domain.mybook.entity.Mybook;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import com.example.ebook.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MybookMapperTest {

    @Test
    @DisplayName("entity_To_MybookDto_Mapper")
    void entityToMybookDto() {

        Mybook mybook = Mybook.builder()
                .id(1L)
                .member(new Member())
                .product(Product.builder().id(1L).subject("product_subject").description("product_description")
                        .price(1000)
                        .member(Member.builder().nickname("member_nickname").build())
                        .build())
                .build();

        MybookDto mybookDto = MybookMapper.INSTANCE.entityToMybookDto(mybook);

        assertAll(
                () -> assertThat(mybookDto.getId()).isEqualTo(mybook.getId()),
                () -> assertThat(mybookDto.getProductId()).isEqualTo(mybook.getProduct().getId()),
                () -> assertThat(mybookDto.getProductSubject()).isEqualTo(mybook.getProduct().getSubject()),
                () -> assertThat(mybookDto.getProductDescription()).isEqualTo(mybook.getProduct().getDescription()),
                () -> assertThat(mybookDto.getProductWriter()).isEqualTo(mybook.getProduct().getMember().getNickname())
        );
    }
}
