package com.example.eBook.domain.order.exception;

public class CashNotEnoughException extends RuntimeException {
    public CashNotEnoughException(String message) {
        super(message);
    }
}
