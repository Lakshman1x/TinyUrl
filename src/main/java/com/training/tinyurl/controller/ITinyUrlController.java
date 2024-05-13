package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface ITinyUrlController {
    public ResponseEntity<String> registerUser(RegistrationReqDto request, BindingResult result)
            throws ValidationException, MongoApiException;
    public ResponseEntity<String> loginUser();
    public ResponseEntity<String> logoutUser();
}
