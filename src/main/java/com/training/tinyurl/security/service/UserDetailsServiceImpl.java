package com.training.tinyurl.security.service;

import com.training.tinyurl.entity.UserInfoEntity;
import com.training.tinyurl.repo.UserInfoRepo;
import com.training.tinyurl.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserInfoRepo userInfoDb;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserInfoEntity> user = userInfoDb.findById(email);
        if(user.isPresent()){
            return new AppUserDetails(user.get());
        }
        throw new UsernameNotFoundException("No record found for "+email);
    }
}
