package com.example.eBook.domain.cart.service;

import com.example.eBook.domain.cart.dto.CartItemDto;
import com.example.eBook.domain.cart.repository.CartRepository;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.global.mapper.CartItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final MemberService memberService;
    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public List<CartItemDto> findAllByUsername(String username) {
        Member member = memberService.findByUsername(username);

        return CartItemMapper.INSTANCE.entitiesToCartItemDtos(
                cartRepository.findAllByMemberAndIsOrdered(member, false));
    }
}
