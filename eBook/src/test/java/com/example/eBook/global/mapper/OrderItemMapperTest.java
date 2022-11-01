package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.order.dto.OrderItemDto;
import com.example.eBook.domain.order.entity.Order;
import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class OrderItemMapperTest {

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

        assertThat(orderItemDto.getId()).isEqualTo(orderItem.getId());
        assertThat(orderItemDto.getProductPrice()).isEqualTo(orderItem.getPrice());
        assertThat(orderItemDto.getProductId()).isEqualTo(orderItem.getId());
        assertThat(orderItemDto.getProductSubject()).isEqualTo(orderItem.getProduct().getSubject());
    }
}
