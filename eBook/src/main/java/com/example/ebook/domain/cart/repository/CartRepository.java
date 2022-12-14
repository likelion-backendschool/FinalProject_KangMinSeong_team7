package com.example.ebook.domain.cart.repository;

import com.example.ebook.domain.cart.entity.CartItem;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByMember(Member member);

    Optional<CartItem> findByMemberAndProduct(Member member, Product product);

    @Transactional
    @Modifying
    @Query("delete from CartItem c where c.member = :member")
    void deleteAllByMemberInBatch(Member member);
}
