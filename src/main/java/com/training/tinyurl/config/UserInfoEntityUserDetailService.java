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
    private UserInfoRepo userInfoDb;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserInfoEntity> user = userInfoDb.findById(email);
        return user.map(UserInfoEntityUserDetails::new).
                orElseThrow(() -> new UsernameNotFoundException("No record found for " + email));
    }
}
