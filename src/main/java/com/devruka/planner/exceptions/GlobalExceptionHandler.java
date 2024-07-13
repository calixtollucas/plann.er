package com.devruka.planner.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        //recupera todos os errors, com seus respectivos campos e mensagens.
        Map<String, String > errors = getErrorsMap(ex.getBindingResult().getFieldErrors());

        //retorna o responseEntity
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationExceptionDTO(errors, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ExceptionDTO> businessExceptionHandler(BusinessException e){

        ExceptionDTO exceptionDTO = new ExceptionDTO(
                e.getExceptionName(),
                e.getMessage(),
                e.getStatus(),
                Instant.now()
                );

        return ResponseEntity.status(exceptionDTO.status()).body(exceptionDTO);
    }

    public Map<String, String > getErrorsMap(List<FieldError> fieldErrors){
        Map<String, String> errorsMap = new HashMap<>();
        //popular map com campo e mensagem
        fieldErrors.forEach(fieldError -> {
            String field = fieldError.getField();
            String fieldErrorMessage = fieldError.getDefaultMessage();

            errorsMap.put(field, fieldErrorMessage);
        });

        return errorsMap;
    }

}
