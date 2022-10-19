package com.example.eBook.domain.product.service;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.product.dto.ProductCreateForm;
import com.example.eBook.domain.product.dto.ProductDetailDto;
import com.example.eBook.domain.product.dto.ProductDto;
import com.example.eBook.domain.product.entity.Product;
import com.example.eBook.domain.product.exception.ProductNotFoundException;
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
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public List<ProductDto> findAll() {
        return ProductMapper.INSTANCE.entitiesToProductDtos(productRepository.findAll());
    }

    public void save(String username, ProductCreateForm productCreateForm) {
        Product product = ProductMapper.INSTANCE.productCreateFormToEntity(productCreateForm);
        Member member = memberService.findByUsername(username);

        product.updateMember(member);
        productRepository.save(product);
    }

    public ProductDetailDto getProductDetail(Long productId) {

        ProductDetailDto productDetailDto = ProductMapper.INSTANCE.entityToProductDetailDto(
                productRepository.findById(productId).orElseThrow(
                        () -> new ProductNotFoundException("해당 상품은 존재하지 않습니다.")));

        productDetailDto.getPostKeyword().getContent();
        return productDetailDto;
    }
}
