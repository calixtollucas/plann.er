package com.devruka.planner.exceptions;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ExceptionDTO(
        String exception_type,
        String message,
        HttpStatus status,
        Instant timestamp
) {
}
