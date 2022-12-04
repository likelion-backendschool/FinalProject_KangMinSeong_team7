package com.example.ebook.domain.order.exception;

public class OrderNotAccessedException extends RuntimeException {
    public OrderNotAccessedException(String message) {
        super(message);
    }
}
