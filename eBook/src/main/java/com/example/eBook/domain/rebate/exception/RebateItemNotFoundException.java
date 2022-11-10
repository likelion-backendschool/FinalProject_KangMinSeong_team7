package com.example.eBook.domain.rebate.exception;

public class RebateItemNotFoundException extends RuntimeException {
    public RebateItemNotFoundException(String message) {
        super(message);
    }
}
