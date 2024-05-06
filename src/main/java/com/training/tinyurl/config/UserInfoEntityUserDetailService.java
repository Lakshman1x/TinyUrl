package com.training.tinyurl.config;

import com.training.tinyurl.entity.UserInfoEntity;
import com.training.tinyurl.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoEntityUserDetailService implements UserDetailsService {
    @Autowired
    UserInfoRepo userInfoDb;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserInfoEntity> listOfUsers = userInfoDb.findById(email);
        return listOfUsers.map(UserInfoEntityUserDetails::new).orElseThrow();
    }
}
