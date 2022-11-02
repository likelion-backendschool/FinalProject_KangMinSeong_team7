package com.example.eBook.global.mapper;

import com.example.eBook.domain.order.dto.OrderItemDto;
import com.example.eBook.domain.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mapping(target = "productSubject", source = "product.subject")
    @Mapping(target = "productPrice", source = "product.price")
    @Mapping(target = "productId", source = "product.id")
    OrderItemDto entityToOrderItemDto(OrderItem orderItem);

    List<OrderItemDto> entitiesToOrderItemDtos(List<OrderItem> orderItems);
}
