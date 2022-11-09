package com.example.eBook.global.api.exception.mybook;

import com.example.eBook.global.api.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class BookNotBelongToMemberException extends ApplicationException {
    private static final String ERROR_CODE = "B-1";
    private static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    private static final String MESSAGE = "해당 도서는 당신의 소유가 아닙니다.";

    public BookNotBelongToMemberException() {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    public BookNotBelongToMemberException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
