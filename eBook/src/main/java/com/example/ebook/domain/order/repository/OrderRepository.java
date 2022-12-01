package com.example.ebook.domain.order.repository;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByMember(Member member);

}
