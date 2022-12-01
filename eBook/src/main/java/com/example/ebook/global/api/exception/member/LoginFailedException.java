package com.example.ebook.global.api.exception.member;

import com.example.ebook.global.api.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class LoginFailedException extends ApplicationException {

    private static final String ERROR_CODE = "M-1";
    private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final String MESSAGE = "로그인 실패입니다.";

    public LoginFailedException() {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    public LoginFailedException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
