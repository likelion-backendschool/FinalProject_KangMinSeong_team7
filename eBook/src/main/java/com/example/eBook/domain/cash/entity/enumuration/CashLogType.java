package com.example.eBook.domain.cash.entity.enumuration;

public enum CashLogType {

    CHARGE_FOR_PAYMENT("상품결제를_위한_충전"), PAYMENT("상품결제"), CHARGE_BY_REFUND("상품환불로_인한_충전"),
    CALCULATE_FROM_SELLER("도서판매자로서_정산받음"), EXCHANGE("환전");

    private final String description;

    CashLogType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}