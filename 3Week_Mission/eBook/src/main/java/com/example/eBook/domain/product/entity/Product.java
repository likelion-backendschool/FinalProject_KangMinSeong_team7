package com.example.eBook.domain.product.entity;

import com.example.eBook.domain.base.BaseTimeEntity;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_keyword_id")
    private PostKeyword postKeyword;

    private String subject;
    @Lob
    private String description;
    private int price;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateProduct(String subject, String description, int price) {
        this.subject = subject;
        this.description = description;
        this.price = price;
    }

    public boolean isOrderable() {
        return true;
    }

    public int getSalePrice() {
        return getPrice();
    }

    public int getWholesalePrice() {
        return (int) Math.ceil(getPrice() * 0.4);
    }
}
