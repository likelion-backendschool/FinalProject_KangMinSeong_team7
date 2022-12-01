package com.example.ebook.domain.withdraw.exception;

public class WithdrawNotBelongToMemberException extends RuntimeException {
    public WithdrawNotBelongToMemberException(String message) {
        super(message);
    }
}
