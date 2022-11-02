package com.example.eBook.domain.order.exception;

public class OrderNotAccessedException extends RuntimeException {
    public OrderNotAccessedException(String message) {
        super(message);
    }
}
