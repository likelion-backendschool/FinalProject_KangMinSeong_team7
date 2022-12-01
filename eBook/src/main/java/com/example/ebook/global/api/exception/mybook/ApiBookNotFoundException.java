package com.example.ebook.global.api.exception.mybook;

import com.example.ebook.global.api.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ApiBookNotFoundException extends ApplicationException {
    private static final String ERROR_CODE = "B-2";
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String MESSAGE = "해당 도서를 찾을 수 없습니다. - 서버 에러";

    public ApiBookNotFoundException() {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    public ApiBookNotFoundException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
