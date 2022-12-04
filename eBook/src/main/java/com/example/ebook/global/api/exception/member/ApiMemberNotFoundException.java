package com.example.ebook.global.api.exception.member;

import com.example.ebook.global.api.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ApiMemberNotFoundException extends ApplicationException {

    private static final String ERROR_CODE = "M-2";
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String MESSAGE = "해당 멤버를 찾을 수 없습니다. - 서버 에러";

    public ApiMemberNotFoundException() {
        super(ERROR_CODE, HTTP_STATUS, MESSAGE);
    }

    public ApiMemberNotFoundException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
