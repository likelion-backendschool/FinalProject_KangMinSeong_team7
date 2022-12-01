package com.example.ebook.domain.order.exception;

public class CashNotEnoughException extends RuntimeException {
    public CashNotEnoughException(String message) {
        super(message);
    }
}
