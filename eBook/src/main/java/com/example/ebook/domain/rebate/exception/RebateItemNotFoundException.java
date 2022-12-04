package com.example.ebook.domain.rebate.exception;

public class RebateItemNotFoundException extends RuntimeException {
    public RebateItemNotFoundException(String message) {
        super(message);
    }
}
