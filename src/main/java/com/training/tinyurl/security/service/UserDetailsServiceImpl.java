package com.training.tinyurl.security.service;

import com.training.tinyurl.entity.UserInfoEntity;
import com.training.tinyurl.repo.UserInfoRepo;
import com.training.tinyurl.security.AppUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserInfoRepo userInfoDb;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final String emailInCaps = email.toUpperCase();
        Optional<UserInfoEntity> user = userInfoDb.findById(emailInCaps);
        if(user.isEmpty()){log.error("User login was unauthorized. No record found for "+emailInCaps);}
        return user.map(AppUserDetails::new).
                orElseThrow(() -> new UsernameNotFoundException("No record found for "+ emailInCaps));
    }
}
