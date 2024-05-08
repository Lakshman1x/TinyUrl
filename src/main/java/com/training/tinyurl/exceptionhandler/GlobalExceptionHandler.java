package com.training.tinyurl.exceptionhandler;

import com.mongodb.MongoException;
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
        return new ResponseEntity<>(errMsg.toString(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<String> handleMongoException(MongoException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MongoApiException.class)
    public ResponseEntity<String> handleMongoApiException(MongoApiException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
