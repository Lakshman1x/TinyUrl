package com.training.tinyurl.controller;

import com.training.tinyurl.constants.RegexpPattern;
import com.training.tinyurl.dto.TinyUrlDto;
import com.training.tinyurl.dto.UpdateUrlDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.QuotaLimitExceededException;
import com.training.tinyurl.exceptionhandler.TooManyCollisionsException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import com.training.tinyurl.security.AppUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.NoSuchAlgorithmException;

public interface ITinyUrlController {


    @PostMapping("getTinyUrl")
    ResponseEntity<String> getTinyUrl(@RequestBody @Valid TinyUrlDto dto, BindingResult result,
                                      @AuthenticationPrincipal AppUserDetails user)
            throws ValidationException, NoSuchAlgorithmException, TooManyCollisionsException, QuotaLimitExceededException;

    @GetMapping("fetch")
    ResponseEntity<String> getFullUrl(@RequestParam @Pattern(regexp = RegexpPattern.BASE64_PATTERN)
                                      String tinyurl) throws MongoApiException;

    @GetMapping("redirect")
    ResponseEntity<Void> redirectToFullUrl(@RequestParam @Pattern(regexp = RegexpPattern.BASE64_PATTERN)
                                           String tinyurl) throws MongoApiException;

    @DeleteMapping("delete")
    ResponseEntity<Void> deleteTinyUrl(@RequestParam @Pattern(regexp = RegexpPattern.BASE64_PATTERN) String tinyurl)
                                                throws MongoApiException;

    @PutMapping("update")
    ResponseEntity<Void> reMapTinyUrl(@RequestBody @Valid UpdateUrlDto dto, BindingResult result)
                                                throws ValidationException, MongoApiException;
}