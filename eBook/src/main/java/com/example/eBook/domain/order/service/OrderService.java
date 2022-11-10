package com.example.eBook.domain.order.service;

import com.example.eBook.domain.cart.entity.CartItem;
import com.example.eBook.domain.cart.service.CartService;
import com.example.eBook.domain.cash.entity.enumuration.CashLogType;
import com.example.eBook.domain.cash.service.CashLogService;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.mybook.service.MybookService;
import com.example.eBook.domain.order.dto.OrderDetailDto;
import com.example.eBook.domain.order.dto.OrderDto;
import com.example.eBook.domain.order.entity.Order;
import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.order.exception.CashNotEnoughException;
import com.example.eBook.domain.order.exception.OrderItemNotFoundException;
import com.example.eBook.domain.order.exception.OrderNotAccessedException;
import com.example.eBook.domain.order.exception.OrderNotFoundException;
import com.example.eBook.domain.order.repository.OrderItemRepository;
import com.example.eBook.domain.order.repository.OrderRepository;
import com.example.eBook.global.mapper.OrderItemMapper;
import com.example.eBook.global.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final MemberService memberService;
    private final CashLogService cashLogService;
    private final MybookService mybookService;

    public Long save(String username) {
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

        cartService.deleteAllByUsername(username);
        return orderRepository.save(order).getId();
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

    @Transactional(readOnly = true)
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("해당 주문은 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public OrderDetailDto getOrderDetail(Long orderId) {

        Order order = findById(orderId);

        OrderDetailDto orderDetailDto = OrderMapper.INSTANCE.entityToOrderItemDto(order);
        orderDetailDto.setOrderItemDtos(OrderItemMapper.INSTANCE.entitiesToOrderItemDtos(order.getOrderItems()));

        return orderDetailDto;
    }

    public void orderByRestCash(String username, Long orderId) {
        Member member = memberService.findByUsername(username);

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("해당 주문은 존재하지 않습니다."));

        int totalPrice = order.getTotalPrice();

        if (member.getRestCash() < totalPrice) {
            throw new CashNotEnoughException("예치금이 부족합니다.");
        }

        member.payRestCash(totalPrice);
        order.setPaymentDone();
        mybookService.save(member, order);
        cashLogService.save(member, CashLogType.PAYMENT_BY_ONLY_CASH, totalPrice * -1);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void orderByTossPayments(String username, Long orderId, int needCash) {
        Member member = memberService.findByUsername(username);

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("해당 주문은 존재하지 않습니다."));

        if (member.getRestCash() < needCash) {
            throw new CashNotEnoughException("예치금이 부족합니다.");
        }

        cashLogService.save(member, CashLogType.CHARGE_FOR_PAYMENT, order.getTotalPrice() - needCash);
        cashLogService.save(member, CashLogType.PAYMENT_BY_TOSSPAYMENTS, (order.getTotalPrice() - needCash) * -1);
        member.payRestCash(needCash);
        order.setPaymentDone();
        mybookService.save(member, order);
        cashLogService.save(member, CashLogType.PAYMENT_BY_ONLY_CASH, needCash * -1);
    }

    public void cancel(Long orderId, String username) {
        Member member = memberService.findByUsername(username);

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("해당 주문은 존재하지 않습니다."));

        if (!order.getMember().equals(member)) {
            throw new OrderNotAccessedException("해당 주문에 접근할 수 없습니다.");
        }

        order.cancelOrder();
    }

    public void refund(Long orderId, String username) {
        Member member = memberService.findByUsername(username);

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("해당 주문은 존재하지 않습니다."));

        if (!order.getMember().equals(member)) {
            throw new OrderNotAccessedException("해당 주문에 접근할 수 없습니다.");
        }

        cashLogService.save(member, CashLogType.CHARGE_BY_REFUND, order.getTotalPrice());
        member.addRestCash(order.getTotalPrice());
        order.refundOrder();

        mybookService.remove(order, member);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findAllByPayDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetween(fromDate, toDate);
    }

    @Transactional(readOnly = true)
    public OrderItem findOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElseThrow(
                () -> new OrderItemNotFoundException("해당 주문 품목을 찾을 수 없습니다."));
    }
}
