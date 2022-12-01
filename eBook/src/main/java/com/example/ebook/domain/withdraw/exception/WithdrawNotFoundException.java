package com.example.ebook.domain.withdraw.exception;

public class WithdrawNotFoundException extends RuntimeException {
    public WithdrawNotFoundException(String message) {
        super(message);
    }
}
