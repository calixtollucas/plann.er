package com.devruka.planner.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ValidationExceptionDTO(
        Map<String, String> errors,
        HttpStatus status
) {
}
