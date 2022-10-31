package com.example.eBook.domain.order.service;

import com.example.eBook.domain.cart.entity.CartItem;
import com.example.eBook.domain.cart.service.CartService;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.order.dto.OrderDto;
import com.example.eBook.domain.order.entity.Order;
import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.order.repository.OrderRepository;
import com.example.eBook.global.mapper.OrderItemMapper;
import com.example.eBook.global.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final MemberService memberService;

    public void save(String username) {
        List<CartItem> cartItems = cartService.findAllByUsername(username);

        List<OrderItem> orderItems = cartItems.stream()
                .filter(c -> c.getProduct().isOrderable())
                .map(c -> new OrderItem(c.getProduct()))
                .toList();

        Order order = Order.builder()
                .member(memberService.findByUsername(username))
                .name(username + "의 주문")
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);
        cartService.deleteAllByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAllByUsername(String username) {
        Member member = memberService.findByUsername(username);

        List<Order> orders = orderRepository.findAllByMember(member);
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderDto orderDto = OrderMapper.INSTANCE.entityToOrderDto(order);
            orderDto.setOrderItemDtos(OrderItemMapper.INSTANCE.entitiesToOrderItemDtos(order.getOrderItems()));
            orderDtos.add(orderDto);
        }

        return orderDtos;
    }
}
