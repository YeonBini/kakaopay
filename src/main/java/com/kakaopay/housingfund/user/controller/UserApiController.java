package com.kakaopay.housingfund.user.controller;

import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import com.kakaopay.housingfund.user.model.Account;
import com.kakaopay.housingfund.user.model.api.request.UserJoinRequest;
import com.kakaopay.housingfund.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("user")
public class UserApiController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserApiController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ApiResult join(@RequestBody UserJoinRequest userJoinRequest) {
        Account account = new Account.Builder()
                .email(userJoinRequest.getEmail())
                .password(passwordEncoder.encode(userJoinRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .roles(userJoinRequest.getRoles())
                .build();
        userService.join(account);

        return OK(account.getEmail());
    }
}
