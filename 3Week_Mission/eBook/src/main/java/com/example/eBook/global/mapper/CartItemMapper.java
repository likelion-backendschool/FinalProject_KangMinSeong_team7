package com.example.eBook.global.mapper;

import com.example.eBook.domain.cart.dto.CartItemDto;
import com.example.eBook.domain.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper {

    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "subject", source = "product.subject")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "description", source = "product.description")
    @Mapping(target = "writer", source = "product.member.nickname")
    CartItemDto entityToCartItemDto(CartItem cartItem);

    List<CartItemDto> entitiesToCartItemDtos(List<CartItem> cartItems);
}
