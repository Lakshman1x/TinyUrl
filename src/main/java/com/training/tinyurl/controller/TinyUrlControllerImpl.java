package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.exceptionalhandler.MongoApiException;
import com.training.tinyurl.exceptionalhandler.ValidationException;
import com.training.tinyurl.service.TinyUrlServiceImpl;
import com.training.tinyurl.util.Validator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/user/")
@Slf4j
public class TinyUrlControllerImpl implements ITinyUrlController {
    private final TinyUrlServiceImpl tinyUrlService;

    public TinyUrlControllerImpl(TinyUrlServiceImpl tinyUrlService) {
        this.tinyUrlService = tinyUrlService;
    }

    @PostMapping("register")
    @Override
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationReqDto request,
                                             BindingResult bindingResult) throws ValidationException, MongoApiException {
        Validator.validate(bindingResult);
        tinyUrlService.createNewUser(request);
        log.info(request.getEmail()+" successfully registered");
        return new ResponseEntity<>(request.getEmail()+" successfully registered",HttpStatus.CREATED);
    }

    @GetMapping("hello")
    public String hello() {
        return "Hello";
    }

}