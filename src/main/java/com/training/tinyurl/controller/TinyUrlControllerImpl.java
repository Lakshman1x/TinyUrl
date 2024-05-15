package com.training.tinyurl.controller;

import com.training.tinyurl.constants.RegexpPattern;
import com.training.tinyurl.dto.TinyUrlDto;
import com.training.tinyurl.dto.UpdateUrlDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.QuotaLimitExceededException;
import com.training.tinyurl.exceptionhandler.TooManyCollisionsException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import com.training.tinyurl.security.AppUserDetails;
import com.training.tinyurl.service.ITinyUrlService;
import com.training.tinyurl.util.Validator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/user/url")
@Slf4j
public class TinyUrlControllerImpl implements ITinyUrlController {
    private final ITinyUrlService tinyUrlService;

    public TinyUrlControllerImpl(ITinyUrlService tinyUrlService) {
        this.tinyUrlService = tinyUrlService;
    }

    @PostMapping("getTinyUrl")
    @Override
    public ResponseEntity<String> getTinyUrl(@RequestBody @Valid TinyUrlDto dto, BindingResult result,
                                             @AuthenticationPrincipal AppUserDetails user)
            throws ValidationException, NoSuchAlgorithmException, TooManyCollisionsException,
            QuotaLimitExceededException {
        Validator.validate(result);
        String tinyurl = tinyUrlService.generateTinyUrl(dto.getLongUrl(), user);
        log.info("Generated tinyurl : " + tinyurl);
        return new ResponseEntity<>(tinyurl, HttpStatus.OK);
    }

    @GetMapping("fetch")
    @Override
    public ResponseEntity<String> getFullUrl(@RequestParam @Pattern(regexp = RegexpPattern.BASE64_PATTERN)
                                                 String tinyurl) throws MongoApiException {
        String fullUrl = tinyUrlService.getLongUrl(tinyurl);
        log.info("Fetching full url for " + tinyurl);
        return new ResponseEntity<>(fullUrl, HttpStatus.OK);
    }

    @GetMapping("redirect")
    @Override
    public ResponseEntity<Void> redirectToFullUrl(@RequestParam @Pattern(regexp = RegexpPattern.BASE64_PATTERN)
                                                      String tinyurl) throws MongoApiException {
        String fullUrl = tinyUrlService.getLongUrl(tinyurl);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, fullUrl);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @DeleteMapping("delete")
    @Override
    public ResponseEntity<Void> deleteTinyUrl(@RequestParam @Pattern(regexp = RegexpPattern.BASE64_PATTERN)
                                                  String tinyurl) throws MongoApiException {
        tinyUrlService.deleteTinyUrl(tinyurl);
        log.info("Deleted " + tinyurl);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("update")
    @PreAuthorize("hasAuthority('PREMIUM')")
    @Override
    public ResponseEntity<Void> reMapTinyUrl(@RequestBody @Valid UpdateUrlDto dto, BindingResult result)
            throws ValidationException, MongoApiException {
        Validator.validate(result);
        String previousUrl = tinyUrlService.getLongUrl(dto.getTinyUrl());
        tinyUrlService.reMapTinyUrl(dto);
        log.info("Remapping " + dto.getTinyUrl() + "to a new url. Previous url : " +previousUrl);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}