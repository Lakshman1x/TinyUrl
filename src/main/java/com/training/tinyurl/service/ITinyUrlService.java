package com.training.tinyurl.service;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.dto.UpdateUrlDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.QuotaLimitExceededException;
import com.training.tinyurl.exceptionhandler.TooManyCollisionsException;
import com.training.tinyurl.security.AppUserDetails;
import java.security.NoSuchAlgorithmException;

public interface ITinyUrlService {
    public String generateTinyUrl(String longUrl, AppUserDetails user)
            throws NoSuchAlgorithmException, TooManyCollisionsException, QuotaLimitExceededException;
    public String getLongUrl(String tinyurl) throws MongoApiException;
    public void deleteTinyUrl(String tinyurl) throws MongoApiException;
    public void reMapTinyUrl(UpdateUrlDto dto) throws MongoApiException;
}