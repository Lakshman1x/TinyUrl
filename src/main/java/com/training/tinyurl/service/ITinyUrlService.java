package com.training.tinyurl.service;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.exceptionalhandler.MongoApiException;

public interface ITinyUrlService {
    void createNewUser(RegistrationReqDto request) throws MongoApiException;
}
