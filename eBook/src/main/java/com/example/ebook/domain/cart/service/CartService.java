package com.example.ebook.domain.cart.service;

import com.example.ebook.domain.cart.dto.CartItemDto;
import com.example.ebook.domain.cart.entity.CartItem;
import com.example.ebook.domain.cart.repository.CartRepository;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.service.MemberService;
import com.example.ebook.domain.product.entity.Product;
import com.example.ebook.domain.product.service.ProductService;
import com.example.ebook.global.mapper.CartItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final MemberService memberService;
    private final ProductService productService;
    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public List<CartItem> findAllByUsername(String username) {
        Member member = memberService.findByUsername(username);

        return cartRepository.findAllByMember(member);
    }

    public List<CartItemDto> cartItemDtoConverter(String username) {
        return CartItemMapper.INSTANCE.entitiesToCartItemDtos(findAllByUsername(username));
    }

    public void save(Long productId, String username) {
        Member member = memberService.findByUsername(username);
        Product product = productService.findById(productId);

        if (cartRepository.findByMemberAndProduct(member, product).isEmpty()) {
            cartRepository.save(CartItem.builder()
                    .product(product)
                    .member(member)
                    .build());
        }
    }

    public void delete(Long productId) {
        cartRepository.deleteById(productId);
    }

    public void deleteAllByUsername(String username) {
        Member member = memberService.findByUsername(username);

        cartRepository.deleteAllByMemberInBatch(member);
    }
}
