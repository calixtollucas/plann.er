package com.devruka.planner.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ExceptionDTO> methodArgumentExceptionHandler(BusinessException e, WebRequest request){

        ExceptionDTO exceptionDTO = new ExceptionDTO(
                e.getExceptionName(),
                e.getMessage(),
                e.getStatus(),
                Instant.now()
                );

        return ResponseEntity.status(exceptionDTO.status()).body(exceptionDTO);
    }

}
