package com.example.eBook.domain.product.service;

import com.example.eBook.domain.product.dto.ProductDto;
import com.example.eBook.domain.product.repository.ProductRepository;
import com.example.eBook.global.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return ProductMapper.INSTANCE.entitiesToProductDtos(productRepository.findAll());
    }
}
