package com.training.tinyurl.exceptionhandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MongoApiException extends Exception{
    private final HttpStatus statusCode;

    public MongoApiException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}