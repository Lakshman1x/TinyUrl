package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.dto.TinyUrlDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import java.security.NoSuchAlgorithmException;

public interface ITinyUrlController {
    public ResponseEntity<String> registerUser(RegistrationReqDto request, BindingResult result)
            throws ValidationException, MongoApiException;
    public ResponseEntity<String> loginUser();
    public ResponseEntity<String>upgradePlan();
    public String getTinyUrl(TinyUrlDto dto, BindingResult result)
            throws ValidationException, NoSuchAlgorithmException;
}
