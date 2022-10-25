package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.product.dto.ProductCreateForm;
import com.example.eBook.domain.product.dto.ProductDetailDto;
import com.example.eBook.domain.product.dto.ProductDto;
import com.example.eBook.domain.product.dto.ProductModifyForm;
import com.example.eBook.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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

        assertThat(productDto.getId()).isEqualTo(product.getId());
        assertThat(productDto.getSubject()).isEqualTo(product.getSubject());
        assertThat(productDto.getPrice()).isEqualTo(product.getPrice());
        assertThat(productDto.getWriter()).isEqualTo(product.getMember().getUsername());
        assertThat(productDto.getCreateDate()).isEqualTo(product.getCreateDate());
    }

    @Test
    @DisplayName("productCreateForm_To_Entity_test")
    public void productCreateFormToEntity() {

        ProductCreateForm productCreateForm = ProductCreateForm.builder()
                .subject("상품명1")
                .description("상품설명1")
                .price(1000)
                .postKeyword(new PostKeyword(1L, "JAVA"))
                .build();

        Product product = ProductMapper.INSTANCE.productCreateFormToEntity(productCreateForm);

        assertThat(product.getSubject()).isEqualTo(productCreateForm.getSubject());
        assertThat(product.getDescription()).isEqualTo(productCreateForm.getDescription());
        assertThat(product.getPrice()).isEqualTo(productCreateForm.getPrice());
        assertThat(product.getPostKeyword()).isEqualTo(productCreateForm.getPostKeyword());
        assertThat(product.getMember()).isNull();
    }

    @Test
    @DisplayName("entity_To_ProductDetailDto_mapper")
    public void entityToProductDetailDto() {

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
        assertThat(productDetailDto.getId()).isEqualTo(product.getId());
        assertThat(productDetailDto.getSubject()).isEqualTo(product.getSubject());
        assertThat(productDetailDto.getDescription()).isEqualTo(product.getDescription());
        assertThat(productDetailDto.getPrice()).isEqualTo(product.getPrice());
        assertThat(productDetailDto.getWriter()).isEqualTo(product.getMember().getUsername());
        assertThat(productDetailDto.getCreateDate()).isEqualTo(product.getCreateDate());
        assertThat(productDetailDto.getPostKeyword()).isEqualTo(product.getPostKeyword());
    }

    @Test
    @DisplayName("entity_To_ProductModifyForm_mapper")
    public void entityToProductModifyForm() {

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

        assertThat(productModifyForm.getSubject()).isEqualTo(product.getSubject());
        assertThat(productModifyForm.getDescription()).isEqualTo(product.getDescription());
        assertThat(productModifyForm.getPrice()).isEqualTo(product.getPrice());
    }
}