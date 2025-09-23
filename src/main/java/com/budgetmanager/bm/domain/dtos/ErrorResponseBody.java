package com.budgetmanager.bm.domain.dtos;

import org.springframework.http.HttpStatus;

public record ErrorResponseBody(
    HttpStatus status,
    String title,
    String message
) {
}