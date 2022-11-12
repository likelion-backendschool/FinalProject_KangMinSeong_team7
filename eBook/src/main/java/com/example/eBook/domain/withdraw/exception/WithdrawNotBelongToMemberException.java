package com.example.eBook.domain.withdraw.exception;

public class WithdrawNotBelongToMemberException extends RuntimeException {
    public WithdrawNotBelongToMemberException(String message) {
        super(message);
    }
}
