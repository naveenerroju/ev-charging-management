package com.naveen.evcharging.charger_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidBootNotification(InvalidInputException ex) {
        return new ResponseEntity<>(
                "{\"status\": \"Error\", \"message\": \"" + ex.getMessage() + "\"}",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(RuntimeException ex) {
        return new ResponseEntity<>(
                "{\"status\": \"Error\", \"message\": \"An unexpected error occurred: " + ex.getMessage() + "\"}",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}