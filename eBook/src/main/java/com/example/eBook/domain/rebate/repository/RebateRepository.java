package com.example.eBook.domain.rebate.repository;

import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.rebate.entity.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RebateRepository extends JpaRepository<RebateOrderItem, Long> {

    Optional<RebateOrderItem> findByOrderItem(OrderItem orderItem);

    @Transactional
    @Modifying
    @Query("delete from RebateOrderItem r where r.orderItem in :removeOrderItems")
    void removeByOrderItem(List<OrderItem> removeOrderItems);
}
