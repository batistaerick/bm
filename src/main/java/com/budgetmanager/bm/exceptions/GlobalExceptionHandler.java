package com.budgetmanager.bm.exceptions;

import static org.springframework.http.ResponseEntity.status;

import com.budgetmanager.bm.domain.dtos.ErrorResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponseBody> handleGlobalException(
        GlobalException exception
    ) {
        ErrorResponseBody body = new ErrorResponseBody(
            exception.getStatus(),
            exception.getTitle(),
            exception.getMessage()
        );
        return status(exception.getStatus()).body(body);
    }
}
