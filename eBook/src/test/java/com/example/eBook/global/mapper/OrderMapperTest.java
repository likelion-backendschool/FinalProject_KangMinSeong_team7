package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.order.dto.OrderDto;
import com.example.eBook.domain.order.entity.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public class OrderMapperTest {

    @Test
    @DisplayName("entity_To_OrderDto_Mapper")
    void entityToOrderDto() {

        Order order = Order.builder()
                .id(1L)
                .member(new Member())
                .name("test_name")
                .orderItems(new ArrayList<>())
                .canceledStatus(false)
                .paidStatus(false)
                .refundedStatus(true)
                .readyStatus(false)
                .payDate(LocalDateTime.now())
                .build();

        OrderDto orderDto = OrderMapper.INSTANCE.entityToOrderDto(order);

        assertThat(orderDto.getId()).isEqualTo(order.getId());
        assertThat(orderDto.getName()).isEqualTo(order.getName());
        assertThat(orderDto.getPayDate()).isEqualTo(order.getPayDate());
        assertThat(orderDto.isReadyStatus()).isEqualTo(order.isReadyStatus());
        assertThat(orderDto.isCanceledStatus()).isEqualTo(order.isCanceledStatus());
        assertThat(orderDto.isPaidStatus()).isEqualTo(order.isPaidStatus());
        assertThat(orderDto.isRefundedStatus()).isEqualTo(order.isRefundedStatus());
    }
}
