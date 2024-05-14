package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.dto.TinyUrlDto;
import com.training.tinyurl.dto.UpdateUrlDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import com.training.tinyurl.security.AppUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import java.security.NoSuchAlgorithmException;

public interface ITinyUrlController {
    public ResponseEntity<String> registerUser(RegistrationReqDto request, BindingResult result)
            throws ValidationException, MongoApiException;
    public ResponseEntity<String> loginUser(AppUserDetails user);
    public ResponseEntity<String> logoutUser(AppUserDetails user);
    public ResponseEntity<String> upgradePlan();
    public ResponseEntity<String> getTinyUrl(TinyUrlDto dto, BindingResult result)
            throws ValidationException, NoSuchAlgorithmException;
    public ResponseEntity<String> getFullUrl(String tinyurl) throws MongoApiException;
    public ResponseEntity<Void> redirectToNewUrl(String tinyUrl) throws MongoApiException;
    public ResponseEntity<Void> deleteTinyUrl(String tinyurl) throws MongoApiException;
    public ResponseEntity<Void> reMapTinyUrl(UpdateUrlDto dto, BindingResult result)
            throws ValidationException, MongoApiException;
}