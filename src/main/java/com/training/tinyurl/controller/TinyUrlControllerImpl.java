package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.dto.TinyUrlDto;
import com.training.tinyurl.entity.TinyUrlEntity;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import com.training.tinyurl.service.ITinyUrlService;
import com.training.tinyurl.util.Validator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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
        log.info("Login successful");
        return new ResponseEntity<>("Login successful",HttpStatus.OK);
    }

    @GetMapping("upgrade")
    @PreAuthorize("hasAuthority('BASIC')")
    @Override
    public ResponseEntity<String>upgradePlan(){
        tinyUrlService.upgradePlan();
        return new ResponseEntity<>("upgrade successful please login again",HttpStatus.OK);
    }

    @PostMapping("gettinyurl")
    @Override
    public ResponseEntity<String> getTinyUrl(@RequestBody @Valid TinyUrlDto dto, BindingResult result)
                                throws ValidationException, NoSuchAlgorithmException {
        Validator.validate(result);
        return new ResponseEntity<>(tinyUrlService.getTinyUrl(dto.getLongUrl()),HttpStatus.OK);
    }

    @GetMapping("fetch")
    @Override
    public ResponseEntity<String> getFullUrl(@RequestParam String tinyurl){
        Optional<TinyUrlEntity> res = tinyUrlService.getUrlEntity(tinyurl);
        if(res.isPresent()){
            return new ResponseEntity<>(res.get().getLongUrl(), HttpStatus.OK);
        }
        log.error("No tinyurl found in the database");
        return new ResponseEntity<>("Tinyurl not found in db", HttpStatus.NOT_FOUND);
    }
}