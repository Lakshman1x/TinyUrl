package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import com.training.tinyurl.service.ITinyUrlService;
import com.training.tinyurl.util.Validator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/user/")
@Slf4j
public class TinyUrlControllerImpl implements ITinyUrlController {
    private final ITinyUrlService tinyUrlService;

    public TinyUrlControllerImpl(ITinyUrlService tinyUrlService) {
        this.tinyUrlService = tinyUrlService;
    }

    @PostMapping("register")
    @Override
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationReqDto request,
                                               BindingResult bindingResult)
                                                throws ValidationException, MongoApiException {
        Validator.validate(bindingResult);
        tinyUrlService.createNewUser(request);
        log.info(request.getEmail()+" successfully registered");
        return new ResponseEntity<>(request.getEmail()+" successfully registered",HttpStatus.CREATED);
    }

    @GetMapping("login")
    @Override
    public ResponseEntity<String> loginUser() {
        String username = tinyUrlService.getLoggedInUserDetails().getUsername();
        log.info(username+" login successful");
        return new ResponseEntity<>(username+" login successful",HttpStatus.OK);
    }

    @GetMapping("logout")
    @Override
    public ResponseEntity<String> logoutUser() {
        String username = tinyUrlService.getLoggedInUserDetails().getUsername();
        tinyUrlService.logoutUser();
        log.info(username+" logged out");
        return new ResponseEntity<>(username+" logged out",HttpStatus.OK);
    }
}