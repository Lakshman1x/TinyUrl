package com.training.tinyurl.service;

import com.training.tinyurl.constants.AccountType;
import com.training.tinyurl.dto.UpdateUrlDto;
import com.training.tinyurl.entity.TinyUrlEntity;
import com.training.tinyurl.entity.UserInfoEntity;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.QuotaLimitExceededException;
import com.training.tinyurl.exceptionhandler.TooManyCollisionsException;
import com.training.tinyurl.repo.TinyUrlRepo;
import com.training.tinyurl.repo.UserInfoRepo;
import com.training.tinyurl.security.AppUserDetails;
import com.training.tinyurl.util.TinyUrlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
public class TinyUrlServiceImpl implements ITinyUrlService{
    private final UserInfoRepo userInfoRepo;
    private final TinyUrlRepo tinyUrlRepo;
    public TinyUrlServiceImpl(UserInfoRepo userInfoDb, TinyUrlRepo tinyUrlRepo) {
        this.userInfoRepo = userInfoDb;
        this.tinyUrlRepo = tinyUrlRepo;
    }

    private boolean isAllowedToGenerateUrl(AppUserDetails user){
        Optional<UserInfoEntity> userInfo = userInfoRepo.findById(user.getEmail());
        final int MAX_QUOTA = 100;
        if(userInfo.isEmpty()){
            throw new IllegalStateException("User details not found in Db");
        }
        return userInfo.get().getAccountType().equals(AccountType.PREMIUM)
                || userInfo.get().getUsedQuota() < MAX_QUOTA;
    }

    private void incrementUserQuota(AppUserDetails userDetails){
        Optional<UserInfoEntity> userInfo = userInfoRepo.findById(userDetails.getEmail());
        if(userInfo.isPresent()) {
            int usedQuota = userInfo.get().getUsedQuota();
            userInfo.get().setUsedQuota(usedQuota + 1);
            userInfoRepo.save(userInfo.get());
        }else{
            throw new IllegalStateException("User details not found in Db");
        }
    }

    @Override
    public String generateTinyUrl(String longUrl, AppUserDetails user)
            throws NoSuchAlgorithmException, TooManyCollisionsException, QuotaLimitExceededException {
        if(!isAllowedToGenerateUrl(user)){
            throw new QuotaLimitExceededException("Please upgrade your plan as you have reached your quota");
        }
        Optional<TinyUrlEntity> result= tinyUrlRepo.findByLongUrl(longUrl);
        if(result.isPresent()){
            return result.get().getTinyUrl();
        }
        String shortCode =TinyUrlGenerator.createShortCode(longUrl);
        final int MAX_ATTEMPT = 3;
        int attempt = 0;
        while (attempt++ < MAX_ATTEMPT && tinyUrlRepo.findById(shortCode).isPresent()){
            shortCode = TinyUrlGenerator.createShortCode(longUrl+attempt);
        }
        if(attempt == MAX_ATTEMPT){
            throw new TooManyCollisionsException(
                    "Failed to generate a unique tinyurl even after "+MAX_ATTEMPT+" tries");
        }
        tinyUrlRepo.save(new TinyUrlEntity(shortCode,longUrl));
        incrementUserQuota(user);
        return shortCode;
    }

    private TinyUrlEntity getTinyUrlEntity(String tinyUrl) throws MongoApiException {
        Optional<TinyUrlEntity> res= tinyUrlRepo.findById(tinyUrl);
        if(res.isPresent()){
            return res.get();
        }
        log.error("No record found for "+tinyUrl);
        throw new MongoApiException(HttpStatus.NOT_FOUND,"No record found for "+tinyUrl);
    }

    @Override
    public String getLongUrl(String tinyUrl) throws MongoApiException {
        return getTinyUrlEntity(tinyUrl).getLongUrl();
    }

    @Override
    public void deleteTinyUrl(String tinyUrl) throws MongoApiException {
        TinyUrlEntity res = getTinyUrlEntity(tinyUrl);
        tinyUrlRepo.delete(res);
    }

    @Override
    public void reMapTinyUrl(UpdateUrlDto dto) throws MongoApiException {
        TinyUrlEntity res = getTinyUrlEntity(dto.getTinyUrl());
        res.setLongUrl(dto.getLongUrl());
        tinyUrlRepo.save(res);
    }
}