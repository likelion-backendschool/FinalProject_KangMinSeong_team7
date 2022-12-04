package com.example.ebook.domain.cash.entity;

import com.example.ebook.domain.base.BaseTimeEntity;
import com.example.ebook.domain.cash.entity.enumuration.CashLogType;
import com.example.ebook.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CashLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cash_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int price;
    @Enumerated(value = EnumType.STRING)
    private CashLogType eventType;
}
