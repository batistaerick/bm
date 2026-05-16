package com.budgetmanager.bm.exceptions;

import static org.springframework.http.ResponseEntity.status;

import com.budgetmanager.bm.domain.dtos.ErrorResponseBody;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> handleValidationException(
        MethodArgumentNotValidException exception
    ) {
        String message = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        ErrorResponseBody body = new ErrorResponseBody(
            HttpStatus.BAD_REQUEST,
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            message
        );
        return status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> handleUsernameNotFoundException(
        UsernameNotFoundException exception
    ) {
        ErrorResponseBody body = new ErrorResponseBody(
            HttpStatus.UNAUTHORIZED,
            HttpStatus.UNAUTHORIZED.getReasonPhrase(),
            exception.getMessage()
        );
        return status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
