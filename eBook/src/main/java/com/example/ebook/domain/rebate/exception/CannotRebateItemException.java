package com.example.ebook.domain.rebate.exception;

public class CannotRebateItemException extends RuntimeException {
    public CannotRebateItemException(String message) {
        super(message);
    }
}
