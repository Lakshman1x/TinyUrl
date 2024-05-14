package com.training.tinyurl.util;

import com.training.tinyurl.exceptionhandler.ValidationException;
import org.springframework.validation.BindingResult;

public class Validator {
    private Validator() {
    }

    public static void validate(BindingResult bind) throws ValidationException {
        if (bind.hasErrors()) {
            throw new ValidationException(bind);
        }
    }
}
