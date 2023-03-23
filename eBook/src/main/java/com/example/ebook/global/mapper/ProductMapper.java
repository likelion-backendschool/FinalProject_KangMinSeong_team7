package com.example.ebook.global.mapper;

import com.example.ebook.domain.product.dto.ProductCreateForm;
import com.example.ebook.domain.product.dto.ProductDetailDto;
import com.example.ebook.domain.product.dto.ProductDto;
import com.example.ebook.domain.product.dto.ProductModifyForm;
import com.example.ebook.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "writer", source = "member.username")
    ProductDto entityToProductDto(Product product);

    List<ProductDto> entitiesToProductDtos(List<Product> productList);

    Product productCreateFormToEntity(ProductCreateForm productCreateForm);

    @Mapping(target = "writer", source = "member.username")
    ProductDetailDto entityToProductDetailDto(Product product);

    ProductModifyForm entityToProductModifyForm(Product product);
}
