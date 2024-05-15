package com.training.tinyurl.service;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.security.AppUserDetails;

public interface IUserService {
    public void createNewUser(RegistrationReqDto request) throws MongoApiException;
    public void upgradePlan(AppUserDetails user);
    public void logoutUser();
}