package com.example.eBook.domain.mybook.service;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.mybook.dto.MybookDto;
import com.example.eBook.domain.mybook.dto.response.MybookResponse;
import com.example.eBook.domain.mybook.dto.response.MybookResponseDto;
import com.example.eBook.domain.mybook.dto.response.MybooksResponse;
import com.example.eBook.domain.mybook.entity.Mybook;
import com.example.eBook.domain.mybook.repository.MybookRepository;
import com.example.eBook.domain.order.entity.Order;
import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.product.entity.Product;
import com.example.eBook.global.api.exception.mybook.ApiBookNotFoundException;
import com.example.eBook.global.api.exception.mybook.BookNotBelongToMemberException;
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

    // REST API

    @Transactional(readOnly = true)
    public MybooksResponse getMybooks(String username) {
        Member member = memberService.findByUsername(username);

        List<MybookResponseDto> mybookResponseDtos = mybookRepository.findAllByMember(member)
                .stream()
                .map(MybookResponseDto::toResponse)
                .toList();

        return new MybooksResponse(mybookResponseDtos);
    }

    @Transactional(readOnly = true)
    public MybookResponse getMybook(String username, Long mybookId) {
        Member member = memberService.findByUsername(username);
        Mybook mybook = mybookRepository.findById(mybookId).orElseThrow(ApiBookNotFoundException::new);

        if (!mybook.getMember().equals(member)) {
            throw new BookNotBelongToMemberException();
        }

        return new MybookResponse(MybookResponseDto.toResponse(mybook));
    }
}
