package com.example.eBook.global.mapper;

import com.example.eBook.domain.order.dto.OrderDetailDto;
import com.example.eBook.domain.order.dto.OrderDto;
import com.example.eBook.domain.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDto entityToOrderDto(Order order);

    @Mapping(target = "buyerUsername", source = "member.username")
    OrderDetailDto entityToOrderItemDto(Order order);
}