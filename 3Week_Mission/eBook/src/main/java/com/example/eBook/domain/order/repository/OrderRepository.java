package com.example.eBook.domain.order.repository;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByMember(Member member);
}
