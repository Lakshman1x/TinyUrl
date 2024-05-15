package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import com.training.tinyurl.security.AppUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
public interface IUserController {

    @PostMapping("register")
    ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationReqDto request,
                                        BindingResult bindingResult)
                                        throws ValidationException, MongoApiException;
    @GetMapping("login")
    ResponseEntity<String> loginUser(@AuthenticationPrincipal AppUserDetails user);
    @GetMapping("logout")
    ResponseEntity<String> logoutUser(@AuthenticationPrincipal AppUserDetails user);
    @PutMapping("upgrade")
    ResponseEntity<String> upgradePlan(@AuthenticationPrincipal AppUserDetails user);
}
