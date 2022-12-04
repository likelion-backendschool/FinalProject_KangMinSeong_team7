package com.example.ebook.domain.cash.entity.enumuration;

public enum CashLogType {

    CHARGE_FOR_PAYMENT("상품결제를_위한_충전"), PAYMENT_BY_TOSSPAYMENTS("토스페이먼츠를_통한_결제"),
    CHARGE_BY_REFUND("상품환불로_인한_충전"), CALCULATE_FROM_SELLER("도서판매자로서_정산받음"),
    EXCHANGE("환전"), PAYMENT_BY_ONLY_CASH("예치금을_통한_결제");

    private final String description;

    CashLogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
