package com.training.tinyurl.service;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.dto.UpdateUrlDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.security.AppUserDetails;
import java.security.NoSuchAlgorithmException;

public interface ITinyUrlService {
    public void createNewUser(RegistrationReqDto request) throws MongoApiException;
    public void upgradePlan(AppUserDetails user);
    public void logoutUser();
    public String genTinyUrl(String longUrl, AppUserDetails user) throws NoSuchAlgorithmException;
    public String getLongLink(String tinyurl) throws MongoApiException;
    public void deleteTinyUrl(String tinyurl) throws MongoApiException;
    public void reMapTinyUrl(UpdateUrlDto dto) throws MongoApiException;
}