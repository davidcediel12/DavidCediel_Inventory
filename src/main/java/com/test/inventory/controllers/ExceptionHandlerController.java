package com.test.inventory.controllers;


import com.test.inventory.dtos.exception.ApplicationException;
import com.test.inventory.dtos.exception.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorDetails> handleApplicationException(
            ApplicationException exception, WebRequest webRequest){

        HttpStatus status = exception.getStatus() != null ?
                exception.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(exception.getErrorDetails(), status);
    }
}
