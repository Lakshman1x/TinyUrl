package com.training.tinyurl.service;

import com.training.tinyurl.constants.AccountType;
import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.entity.TinyUrlEntity;
import com.training.tinyurl.entity.UserInfoEntity;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.repo.TinyUrlRepo;
import com.training.tinyurl.repo.UserInfoRepo;
import com.training.tinyurl.security.AppUserDetails;
import com.training.tinyurl.util.TinyUrlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
public class TinyUrlServiceImpl implements ITinyUrlService{

    private final UserInfoRepo userInfoRepo;
    private final PasswordEncoder encoder;
    private final TinyUrlRepo tinyUrlRepo;

    public TinyUrlServiceImpl(UserInfoRepo userInfoDb, PasswordEncoder encoder, TinyUrlRepo tinyUrlRepo) {
        this.userInfoRepo = userInfoDb;
        this.encoder = encoder;
        this.tinyUrlRepo = tinyUrlRepo;
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
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private boolean isAllowedToGenUrl(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Optional<UserInfoEntity> userInfo = userInfoRepo.findById(userDetails.getEmail());
        return userInfo.get().getAccountType().toString().equals("PREMIUM")
                || userInfo.get().getUsedQuota() < 100;
    }
    private void incrementUserQuota(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Optional<UserInfoEntity> userInfo = userInfoRepo.findById(userDetails.getEmail());
        if(userInfo.isPresent()) {
            int usedQuota = userInfo.get().getUsedQuota();
            userInfo.get().setUsedQuota(usedQuota + 1);
            userInfoRepo.save(userInfo.get());
        }
    }

    public String getTinyUrl(String longUrl) throws NoSuchAlgorithmException {
        if(!isAllowedToGenUrl()){
            return "Please upgrade your plan as you have reached your quota";}

        Optional<TinyUrlEntity> result= tinyUrlRepo.findByLongUrl(longUrl);
        if(result.isPresent()){
            return result.get().getTinyUrl();
        }
        String shortCode =TinyUrlGenerator.createShortCode(longUrl);
        tinyUrlRepo.save(new TinyUrlEntity(shortCode,longUrl));
        incrementUserQuota();
        return shortCode;
    }

    public Optional<TinyUrlEntity> getUrlEntity(String tinyurl){
        return tinyUrlRepo.findById(tinyurl);
    }
}