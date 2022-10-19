package com.example.eBook.global.mapper;

import com.example.eBook.domain.product.dto.ProductCreateForm;
import com.example.eBook.domain.product.dto.ProductDto;
import com.example.eBook.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "writer", source = "member.nickname")
    ProductDto entityToProductDto(Product product);

    List<ProductDto> entitiesToProductDtos(List<Product> productList);

    Product productCreateFormToEntity(ProductCreateForm productCreateForm);
}
