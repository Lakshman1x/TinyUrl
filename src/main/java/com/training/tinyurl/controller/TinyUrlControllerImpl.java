package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.dto.TinyUrlDto;
import com.training.tinyurl.dto.UpdateUrlDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import com.training.tinyurl.security.AppUserDetails;
import com.training.tinyurl.service.ITinyUrlService;
import com.training.tinyurl.util.Validator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.NoSuchAlgorithmException;

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
    public ResponseEntity<String> loginUser(@AuthenticationPrincipal AppUserDetails user) {
        String username = user.getUsername();
        log.info(username+" login successful");
        return new ResponseEntity<>("Login successful",HttpStatus.OK);
    }

    @GetMapping("logout")
    @Override
    public ResponseEntity<String> logoutUser(@AuthenticationPrincipal AppUserDetails user) {
        String username= user.getUsername();
        tinyUrlService.logoutUser();
        log.info(username+" logged out");
        return new ResponseEntity<>("Logged out",HttpStatus.OK);
    }

    @PutMapping("upgradePlan")
    @PreAuthorize("hasAuthority('BASIC')")
    @Override
    public ResponseEntity<String> upgradePlan(){
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
    public ResponseEntity<String> getFullUrl(@RequestParam String tinyurl) throws MongoApiException {
        String fullUrl = tinyUrlService.getLongLink(tinyurl);
        return new ResponseEntity<>(fullUrl, HttpStatus.OK);
    }

    @GetMapping("redirect")
    @Override
    public ResponseEntity<Void> redirectToNewUrl(@RequestParam String tinyurl) throws MongoApiException {
        String fullUrl = tinyUrlService.getLongLink(tinyurl);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, fullUrl);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @DeleteMapping("delete")
    @Override
    public ResponseEntity<Void> deleteTinyUrl(@RequestParam String tinyurl) throws MongoApiException {
       tinyUrlService.deleteTinyUrl(tinyurl);
       return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("update")
    @PreAuthorize("hasAuthority('PREMIUM')")
    @Override
    public ResponseEntity<Void> reMapTinyUrl(@RequestBody @Valid UpdateUrlDto dto, BindingResult result)
            throws ValidationException, MongoApiException {
        Validator.validate(result);
        tinyUrlService.reMapTinyUrl(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}