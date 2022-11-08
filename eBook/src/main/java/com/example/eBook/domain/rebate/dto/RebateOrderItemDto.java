package com.example.eBook.domain.rebate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RebateOrderItemDto {

    private Long id;
    private Long orderItemId;
    private LocalDateTime payDate;
    private String productSubject;
    private int payPrice;
    private int pgFee;
    private int refundPrice;
    private String sellerName;
    private LocalDateTime rebateDate;
    private Long rebateCashLogId;
    private boolean rebateAvailable;
    private int rebatePrice;
}
