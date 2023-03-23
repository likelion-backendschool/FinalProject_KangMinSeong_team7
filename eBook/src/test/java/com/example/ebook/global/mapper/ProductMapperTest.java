package com.example.ebook.global.mapper;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import com.example.ebook.domain.product.dto.ProductCreateForm;
import com.example.ebook.domain.product.dto.ProductDetailDto;
import com.example.ebook.domain.product.dto.ProductDto;
import com.example.ebook.domain.product.dto.ProductModifyForm;
import com.example.ebook.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @Test
    @DisplayName("entity_To_ProductDto_Mapper")
    void entityToProductDto() {
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

        assertAll(
                () -> assertThat(productDto.getId()).isEqualTo(product.getId()),
                () -> assertThat(productDto.getSubject()).isEqualTo(product.getSubject()),
                () -> assertThat(productDto.getPrice()).isEqualTo(product.getPrice()),
                () -> assertThat(productDto.getWriter()).isEqualTo(product.getMember().getUsername()),
                () -> assertThat(productDto.getCreateDate()).isEqualTo(product.getCreateDate())
        );
    }

    @Test
    @DisplayName("productCreateForm_To_Entity_test")
    void productCreateFormToEntity() {

        ProductCreateForm productCreateForm = ProductCreateForm.builder()
                .subject("상품명1")
                .description("상품설명1")
                .price(1000)
                .build();

        Product product = ProductMapper.INSTANCE.productCreateFormToEntity(productCreateForm);

        assertAll(
                () -> assertThat(product.getSubject()).isEqualTo(productCreateForm.getSubject()),
                () -> assertThat(product.getDescription()).isEqualTo(productCreateForm.getDescription()),
                () -> assertThat(product.getPrice()).isEqualTo(productCreateForm.getPrice()),
                () -> assertThat(product.getMember()).isNull()
        );
    }

    @Test
    @DisplayName("entity_To_ProductDetailDto_mapper")
    void entityToProductDetailDto() {

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

        ProductDetailDto productDetailDto = ProductMapper.INSTANCE.entityToProductDetailDto(product);

        assertAll(
                () -> assertThat(productDetailDto.getId()).isEqualTo(product.getId()),
                () -> assertThat(productDetailDto.getSubject()).isEqualTo(product.getSubject()),
                () -> assertThat(productDetailDto.getDescription()).isEqualTo(product.getDescription()),
                () -> assertThat(productDetailDto.getPrice()).isEqualTo(product.getPrice()),
                () -> assertThat(productDetailDto.getWriter()).isEqualTo(product.getMember().getUsername()),
                () -> assertThat(productDetailDto.getCreateDate()).isEqualTo(product.getCreateDate())
        );
    }

    @Test
    @DisplayName("entity_To_ProductModifyForm_mapper")
    void entityToProductModifyForm() {

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

        ProductModifyForm productModifyForm = ProductMapper.INSTANCE.entityToProductModifyForm(product);

        assertAll(
                () -> assertThat(productModifyForm.getSubject()).isEqualTo(product.getSubject()),
                () -> assertThat(productModifyForm.getDescription()).isEqualTo(product.getDescription()),
                () -> assertThat(productModifyForm.getPrice()).isEqualTo(product.getPrice())
        );
    }
}