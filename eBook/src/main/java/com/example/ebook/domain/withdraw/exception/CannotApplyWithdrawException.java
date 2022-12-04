package com.example.ebook.domain.withdraw.exception;

public class CannotApplyWithdrawException extends RuntimeException {
    public CannotApplyWithdrawException(String message) {
        super(message);
    }
}
