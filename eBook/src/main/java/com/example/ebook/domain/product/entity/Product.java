package com.example.ebook.domain.product.entity;

import com.example.ebook.domain.base.BaseTimeEntity;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
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
