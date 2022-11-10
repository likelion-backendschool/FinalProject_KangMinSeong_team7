package com.example.eBook.domain.rebate.exception;

public class CannotRebateItemException extends RuntimeException {
    public CannotRebateItemException(String message) {
        super(message);
    }
}
