package com.example.eBook.domain.mybook.service;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.mybook.dto.MybookDto;
import com.example.eBook.domain.mybook.entity.Mybook;
import com.example.eBook.domain.mybook.repository.MybookRepository;
import com.example.eBook.domain.order.entity.Order;
import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.product.entity.Product;
import com.example.eBook.global.mapper.MybookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MybookService {

    private final MybookRepository mybookRepository;
    private final MemberService memberService;

    public void save(Member member, Order order) {
        List<Mybook> mybooks = new ArrayList<>();

        List<Product> products = order.getOrderItems().stream()
                .map(OrderItem::getProduct)
                .toList();

        for (Product product : products) {
            mybooks.add(Mybook.builder()
                    .product(product).member(member).build());
        }

        mybookRepository.saveAll(mybooks);
    }

    @Transactional(readOnly = true)
    public List<MybookDto> findAllBooksByUsername(String username) {
        Member member = memberService.findByUsername(username);

        return MybookMapper.INSTANCE.entitiesToMybookDtos(mybookRepository.findAllByMember(member));
    }

    public void remove(Order order, Member member) {

        List<Product> productIds = order.getOrderItems().stream()
                .map(o -> o.getProduct())
                .toList();

        mybookRepository.removeByRefund(member, productIds);
    }
}
