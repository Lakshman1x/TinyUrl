package com.training.tinyurl.service;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import java.security.NoSuchAlgorithmException;

public interface ITinyUrlService {
    public void createNewUser(RegistrationReqDto request) throws MongoApiException;

    public void upgradePlan();

    public String getTinyUrl(String longUrl) throws NoSuchAlgorithmException;
}
