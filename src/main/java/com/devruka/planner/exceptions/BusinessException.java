package com.devruka.planner.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessException extends RuntimeException{

    private String exceptionName;
    private HttpStatus status;

    public BusinessException(Exception ex,String message, HttpStatus status) {
        super(message);
        this.exceptionName = ex.getClass().getName();
        this.status = status;
    }

}


