package com.training.tinyurl.exceptionhandler;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationException extends Exception {

    private final transient BindingResult bindingResult;
    public ValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
