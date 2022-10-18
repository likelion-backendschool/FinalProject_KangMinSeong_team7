package com.example.eBook.domain.member.exception;

public class PasswordNotSameException extends RuntimeException {
    public PasswordNotSameException(String message) {
        super(message);
    }
}
