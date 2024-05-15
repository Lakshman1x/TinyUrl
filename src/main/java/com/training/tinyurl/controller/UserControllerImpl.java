package com.training.tinyurl.controller;

import com.training.tinyurl.dto.RegistrationReqDto;
import com.training.tinyurl.exceptionhandler.MongoApiException;
import com.training.tinyurl.exceptionhandler.ValidationException;
import com.training.tinyurl.security.AppUserDetails;
import com.training.tinyurl.service.IUserService;
import com.training.tinyurl.util.Validator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/")
@Slf4j
public class UserControllerImpl implements IUserController{
    private final IUserService userService;
    public UserControllerImpl(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    @Override
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationReqDto request,
                                               BindingResult bindingResult)
            throws ValidationException, MongoApiException {
        Validator.validate(bindingResult);
        userService.createNewUser(request);
        log.info(request.getEmail()+" successfully registered");
        return new ResponseEntity<>(request.getEmail()+" successfully registered", HttpStatus.CREATED);
    }

    @GetMapping("login")
    @Override
    public ResponseEntity<String> loginUser(@AuthenticationPrincipal AppUserDetails user) {
        String username = user.getUsername();
        log.info(username+" login successful");
        return new ResponseEntity<>("Login successful",HttpStatus.OK);
    }

    @GetMapping("logout")
    @Override
    public ResponseEntity<String> logoutUser(@AuthenticationPrincipal AppUserDetails user) {
        String username= user.getUsername();
        userService.logoutUser();
        log.info(username+" logged out");
        return new ResponseEntity<>("Logged out",HttpStatus.OK);
    }

    @PutMapping("upgradePlan")
    @PreAuthorize("hasAuthority('BASIC')")
    @Override
    public ResponseEntity<String> upgradePlan(@AuthenticationPrincipal AppUserDetails user){
        userService.upgradePlan(user);
        log.info("Plan upgrade for "+user.getEmail());
        return new ResponseEntity<>("upgrade successful please login again",HttpStatus.OK);
    }

}
