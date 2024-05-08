package com.training.tinyurl.service;

import com.training.tinyurl.constants.AccountType;
import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.entity.UserInfoEntity;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.repo.UserInfoRepo;
import com.training.tinyurl.security.AppUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TinyUrlServiceImpl implements ITinyUrlService{

    private final UserInfoRepo userInfoRepo;
    private final PasswordEncoder encoder;

    public TinyUrlServiceImpl(UserInfoRepo userInfoDb, PasswordEncoder encoder) {
        this.userInfoRepo = userInfoDb;
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
    public  void upgradePlan(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        userInfoRepo.save(new UserInfoEntity(
                userDetails.getEmail(),
                userDetails.getPassword(),
                AccountType.PREMIUM
        ));
    }

}