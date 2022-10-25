package com.example.eBook.domain.cart.repository;

import com.example.eBook.domain.cart.entity.CartItem;
import com.example.eBook.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByMemberAndIsOrdered(Member member, boolean isOrdered);
}
