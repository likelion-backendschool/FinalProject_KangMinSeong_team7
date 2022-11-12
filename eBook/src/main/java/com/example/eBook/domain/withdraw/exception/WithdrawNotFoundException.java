package com.example.eBook.domain.withdraw.exception;

public class WithdrawNotFoundException extends RuntimeException {
    public WithdrawNotFoundException(String message) {
        super(message);
    }
}
