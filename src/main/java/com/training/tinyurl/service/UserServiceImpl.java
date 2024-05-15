package com.training.tinyurl.service;

import com.training.tinyurl.constants.AccountType;
import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.entity.UserInfoEntity;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.repo.UserInfoRepo;
import com.training.tinyurl.security.AppUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements IUserService{

    private final UserInfoRepo userInfoRepo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserInfoRepo userInfoRepo, PasswordEncoder encoder) {
        this.userInfoRepo = userInfoRepo;
        this.encoder = encoder;
    }

    @Override
    public void createNewUser(RegistrationReqDto request) throws MongoApiException {
        request.setEmail(request.getEmail().toUpperCase());
        if(!userInfoRepo.existsById(request.getEmail())){
            userInfoRepo.save(new UserInfoEntity(
                    request.getEmail(),
                    encoder.encode(request.getPassword()),
                    request.getAccountType()));
            return;
        }
        log.error("User with same email already exists");
        throw new MongoApiException(HttpStatus.CONFLICT,"User with same email already exists");
    }

    @Override
    public void upgradePlan(AppUserDetails userDetails){
        userInfoRepo.save(new UserInfoEntity(
                userDetails.getEmail(),
                userDetails.getPassword(),
                AccountType.PREMIUM
        ));
        // we need to login again once the authorities are changed
        logoutUser();
    }

    @Override
    public void logoutUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}