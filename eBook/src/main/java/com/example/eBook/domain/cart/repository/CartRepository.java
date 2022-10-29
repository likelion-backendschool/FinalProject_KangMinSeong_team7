package com.example.eBook.domain.cart.repository;

import com.example.eBook.domain.cart.entity.CartItem;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByMember(Member member);

    Optional<CartItem> findByMemberAndProduct(Member member, Product product);
}
