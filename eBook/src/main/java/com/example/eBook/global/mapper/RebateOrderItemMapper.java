package com.example.eBook.global.mapper;

import com.example.eBook.domain.rebate.dto.RebateOrderItemDto;
import com.example.eBook.domain.rebate.entity.RebateOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RebateOrderItemMapper {

    RebateOrderItemMapper INSTANCE = Mappers.getMapper(RebateOrderItemMapper.class);

    @Mapping(target = "orderItemId", source = "orderItem.id")
    @Mapping(target = "productSubject", source = "product.subject")
    @Mapping(target = "rebateCashLogId", source = "rebateCashLog.id")
    @Mapping(target = "rebatePrice", source = "rebateOrderItem", qualifiedByName = "calculateRebatePrice")
    @Mapping(target = "rebateAvailable", source = "rebateOrderItem", qualifiedByName = "isRebateAvailable")
    RebateOrderItemDto entityToRebateOrderItemDto(RebateOrderItem rebateOrderItem);

    List<RebateOrderItemDto> entitiesToRebateOrderItemDtos(List<RebateOrderItem> rebateOrderItem);

    @Named("calculateRebatePrice")
    static int calculateRebatePrice(RebateOrderItem rebateOrderItem) {
        if (rebateOrderItem.getRefundPrice() > 0) {
            return 0;
        }

        return rebateOrderItem.getWholesalePrice() - rebateOrderItem.getPgFee();
    }

    @Named("isRebateAvailable")
    static boolean isRebateAvailable(RebateOrderItem rebateOrderItem) {
        return rebateOrderItem.getRefundPrice() <= 0 && rebateOrderItem.getRebateDate() == null;
    }
}
