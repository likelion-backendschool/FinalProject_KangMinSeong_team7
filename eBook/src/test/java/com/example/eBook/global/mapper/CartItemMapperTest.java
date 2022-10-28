package com.example.eBook.global.mapper;

import com.example.eBook.domain.cart.dto.CartItemDto;
import com.example.eBook.domain.cart.entity.CartItem;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class CartItemMapperTest {

    @Test
    @DisplayName("엔티티_To_CartItem_Mapper")
    void entityToCartItemDto() {

        Member member = new Member(1L, "test_username", "1234", "test_nickname", "test@email.com", 3L, LocalDateTime.now());
        CartItem cartItem = CartItem.builder()
                .id(1L)
                .member(member)
                .product(new Product(1L, member, null, "test_subject", "test_description", 1000))
                .build();

        CartItemDto cartItemDto = CartItemMapper.INSTANCE.entityToCartItemDto(cartItem);

        assertThat(cartItemDto.getId()).isEqualTo(cartItem.getId());
        assertThat(cartItemDto.getPrice()).isEqualTo(cartItem.getProduct().getPrice());
        assertThat(cartItemDto.getProductId()).isEqualTo(cartItem.getProduct().getId());
        assertThat(cartItemDto.getSubject()).isEqualTo(cartItem.getProduct().getSubject());
        assertThat(cartItemDto.getWriter()).isEqualTo(cartItem.getMember().getNickname());
    }
}
