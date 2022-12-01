package com.example.ebook.global.api.exception;

import com.example.ebook.global.api.reponse.dto.FailResponse;
import com.example.ebook.global.api.reponse.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = {RestController.class})
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ResponseService responseService;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<FailResponse> applicationException(ApplicationException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(responseService.getFailResponse(e.getMessage(), e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseService.getFailResponse(e.getMessage(), "CLIENT_ERROR_1"));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<FailResponse> DataAccessException(DataAccessException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseService.getFailResponse(e.getMessage() + " server error", "SERVER_ERROR_1"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FailResponse> RuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseService.getFailResponse(e.getMessage() + " server error", "SERVER_ERROR_2"));
    }
}
