package com.training.tinyurl.exceptionalhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        StringBuilder errMsg = new StringBuilder("Validation error : \n");
        for (FieldError err : ex.getBindingResult().getFieldErrors()) {
            errMsg.append(err.getDefaultMessage()).append("\n");
            log.warn(err.getDefaultMessage());
        }
        return new ResponseEntity<>(errMsg.toString(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MongoApiException.class)
    public ResponseEntity<String> handleValidationException(MongoApiException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
