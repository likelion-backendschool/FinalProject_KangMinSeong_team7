package com.example.ebook.global.mapper;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.order.dto.OrderItemDto;
import com.example.ebook.domain.order.entity.Order;
import com.example.ebook.domain.order.entity.OrderItem;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import com.example.ebook.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemMapperTest {

    @Test
    @DisplayName("entity_To_OrderItemDto_Mapper")
    void entityToOrderItemDto() {

        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .order(new Order())
                .product(new Product(1L, new Member(), new PostKeyword(), "test_subject", "test_description", 1000))
                .payDate(LocalDateTime.now())
                .price(1000)
                .salePrice(1000)
                .build();

        OrderItemDto orderItemDto = OrderItemMapper.INSTANCE.entityToOrderItemDto(orderItem);

        assertAll(
                () -> assertThat(orderItemDto.getId()).isEqualTo(orderItem.getId()),
                () -> assertThat(orderItemDto.getProductPrice()).isEqualTo(orderItem.getPrice()),
                () -> assertThat(orderItemDto.getProductId()).isEqualTo(orderItem.getId()),
                () ->assertThat(orderItemDto.getProductSubject()).isEqualTo(orderItem.getProduct().getSubject())
        );
    }
}
