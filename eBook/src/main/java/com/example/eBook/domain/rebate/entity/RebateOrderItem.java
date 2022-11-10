package com.example.eBook.domain.rebate.entity;

import com.example.eBook.domain.base.BaseTimeEntity;
import com.example.eBook.domain.cash.entity.CashLog;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.order.entity.Order;
import com.example.eBook.domain.order.entity.OrderItem;
import com.example.eBook.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RebateOrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    private int price;
    private int salePrice;
    private int wholesalePrice;
    private int pgFee;
    private int payPrice;
    private int refundPrice;
    private boolean isPaid;
    private LocalDateTime payDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CashLog rebateCashLog;
    private LocalDateTime rebateDate;

    private String productSubject;

    private LocalDateTime orderItemCreateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member buyer;
    private String buyerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member seller;
    private String sellerName;

    public RebateOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        order = orderItem.getOrder();
        product = orderItem.getProduct();
        price = orderItem.getPrice();
        salePrice = orderItem.getSalePrice();
        wholesalePrice = orderItem.getWholesalePrice();
        pgFee = orderItem.getPgFee();
        payPrice = orderItem.getPayPrice();
        refundPrice = orderItem.getRefundPrice();
        isPaid = orderItem.isPaid();
        payDate = orderItem.getPayDate();

        productSubject = orderItem.getProduct().getSubject();

        orderItemCreateDate = orderItem.getCreateDate();

        buyer = orderItem.getOrder().getMember();
        buyerName = orderItem.getOrder().getMember().getUsername();

        seller = orderItem.getProduct().getMember();
        sellerName = orderItem.getProduct().getMember().getNickname();
    }

    public boolean isRebateAvailable() {
        return refundPrice <= 0 && rebateDate == null;
    }

    public boolean isRebateDone() {
        return rebateDate != null;
    }

    public int calculateRebatePrice() {
        if (refundPrice > 0) {
            return 0;
        }

        return wholesalePrice - pgFee;
    }

    public void setRebateDone(CashLog cashLog) {
        this.rebateDate = LocalDateTime.now();
        this.rebateCashLog = cashLog;
    }
}
