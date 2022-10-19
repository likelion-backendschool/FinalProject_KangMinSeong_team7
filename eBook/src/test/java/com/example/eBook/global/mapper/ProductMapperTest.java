package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.product.dto.ProductDto;
import com.example.eBook.domain.product.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductMapperTest {

    @Test
    @DisplayName("entity_To_ProductDto_Mapper")
    public void entityToProductDto() {
        Member member = Member.builder()
                .username("test_username")
                .password("1234")
                .email("test@email.com")
                .nickname("test_nickname")
                .authLevel(3L)
                .build();

        Product product = Product.builder()
                .id(1L)
                .subject("글제목")
                .description("상품 설명")
                .member(member)
                .price(10000)
                .build();

        ProductDto productDto = ProductMapper.INSTANCE.entityToProductDto(product);

        Assertions.assertThat(productDto.getId()).isEqualTo(product.getId());
        Assertions.assertThat(productDto.getSubject()).isEqualTo(product.getSubject());
        Assertions.assertThat(productDto.getPrice()).isEqualTo(product.getPrice());
        Assertions.assertThat(productDto.getWriter()).isEqualTo(product.getMember().getNickname());
        Assertions.assertThat(productDto.getCreateDate()).isEqualTo(product.getCreateDate());
    }
}