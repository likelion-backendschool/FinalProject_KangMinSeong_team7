package com.example.eBook.domain.product.entity;

import com.example.eBook.domain.base.BaseTimeEntity;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private PostKeyword postKeyword;

    private String subject;
    private String description;
    private int price;
}
