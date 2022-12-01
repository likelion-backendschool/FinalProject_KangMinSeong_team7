package com.example.ebook.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {

    private Long id;
    private String name;

    private String buyerUsername;
    private List<OrderItemDto> orderItemDtos;
    private LocalDateTime payDate;
    private boolean readyStatus;
    private boolean paidStatus;
    private boolean canceledStatus;
    private boolean refundedStatus;

    public int calculatePayPrice() {
        int totalPrice = 0;
        for (OrderItemDto orderItemDto : orderItemDtos) {
            totalPrice += orderItemDto.getProductPrice();
        }
        return totalPrice;
    }
}
