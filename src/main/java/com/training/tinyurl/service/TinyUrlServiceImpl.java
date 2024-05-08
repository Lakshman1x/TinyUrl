package com.training.tinyurl.service;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.entity.UserInfoEntity;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.repo.UserInfoRepo;
import lombok.extern.slf4j.Slf4j;
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
        if(!userInfoRepo.existsById(request.getEmail())){
            userInfoRepo.save(new UserInfoEntity(
                    request.getEmail(),
                    encoder.encode(request.getPassword()),
                    request.getAccountType()));
            return;
        }
        log.error("User with same email already exists");
        throw new MongoApiException("User with same email already exists");
    }

}