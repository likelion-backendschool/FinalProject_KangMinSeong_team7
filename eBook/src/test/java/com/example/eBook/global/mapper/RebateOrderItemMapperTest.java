package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.order.entity.Order;
import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.product.entity.Product;
import com.example.eBook.domain.rebate.dto.RebateOrderItemDto;
import com.example.eBook.domain.rebate.entity.RebateOrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public class RebateOrderItemMapperTest {

    @Test
    @DisplayName("entity_To_RebateOrderItemDto_Mapper")
    void entityToRebateOrderItemDto() {

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

        RebateOrderItem rebateOrderItem = new RebateOrderItem(OrderItem.builder()
                .id(1L)
                .product(Product.builder().subject("product_subject")
                        .member(Member.builder().username("test_username").nickname("test_nickname")
                                .build()).build())
                .payDate(LocalDateTime.now())
                .price(1000)
                .salePrice(1000)
                .wholesalePrice(1000)
                .pgFee(100)
                .refundPrice(0)
                .order(order)
                .build());

        RebateOrderItemDto rebateOrderItemDto = RebateOrderItemMapper.INSTANCE.entityToRebateOrderItemDto(rebateOrderItem);

        assertThat(rebateOrderItemDto.getOrderItemId()).isEqualTo(rebateOrderItem.getOrderItem().getId());
        assertThat(rebateOrderItemDto.getPayDate()).isEqualTo(rebateOrderItem.getPayDate());
        assertThat(rebateOrderItemDto.getRebateDate()).isEqualTo(rebateOrderItem.getRebateDate());
        assertThat(rebateOrderItemDto.getPayPrice()).isEqualTo(rebateOrderItem.getPayPrice());
        assertThat(rebateOrderItemDto.getPgFee()).isEqualTo(rebateOrderItem.getPgFee());
        assertThat(rebateOrderItemDto.getRefundPrice()).isEqualTo(rebateOrderItem.getRefundPrice());
        assertThat(rebateOrderItemDto.getSellerName()).isEqualTo(rebateOrderItem.getSellerName());
        assertThat(rebateOrderItemDto.isRebateAvailable()).isEqualTo(true);
        assertThat(rebateOrderItemDto.getRebatePrice()).isEqualTo(900);
    }
}
