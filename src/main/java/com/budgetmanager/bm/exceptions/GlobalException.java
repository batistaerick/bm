package com.budgetmanager.bm.exceptions;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;

@Getter
@Log4j2
public class GlobalException extends RuntimeException {

    private final HttpStatus status;
    private final String title;

    public GlobalException(
        Throwable throwable,
        HttpStatus status,
        String message,
        Object... objects
    ) {
        super(MessageFormatter.arrayFormat(message, objects).getMessage());
        this.status = status;
        this.title = status.getReasonPhrase();
        log.error(throwable);
    }

    public GlobalException(
        HttpStatus status,
        String message,
        Object... objects
    ) {
        super(MessageFormatter.arrayFormat(message, objects).getMessage());
        this.status = status;
        this.title = status.getReasonPhrase();
        log.error(message);
    }
}
