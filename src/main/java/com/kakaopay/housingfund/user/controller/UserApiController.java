package com.kakaopay.housingfund.user.controller;

import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import com.kakaopay.housingfund.security.JwtTokenProvider;
import com.kakaopay.housingfund.user.model.Account;
import com.kakaopay.housingfund.user.model.api.request.UserJoinRequest;
import com.kakaopay.housingfund.user.model.api.request.UserLoginRequest;
import com.kakaopay.housingfund.user.model.api.response.UserSignInResponse;
import com.kakaopay.housingfund.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("user")
public class UserApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public UserApiController(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("signup")
    public ApiResult join(@RequestBody UserJoinRequest userJoinRequest) {
        Account account = new Account.Builder()
                .email(userJoinRequest.getEmail())
                .password(userJoinRequest.getPassword())
                .createdAt(LocalDateTime.now())
                .roles(userJoinRequest.getRoles())
                .build();
        userService.join(account);

        return OK(account.getEmail());
    }

    @PostMapping("signin")
    public ApiResult login(@RequestBody UserLoginRequest userLoginRequest) {
        Account account = userService.login(userLoginRequest.getEmail(), userLoginRequest.getPassword());
        final String token = jwtTokenProvider.createToken(account.getEmail(), account.getRoles());
        UserSignInResponse userSignInResponse = new UserSignInResponse(account.getEmail(), token);
        return OK(userSignInResponse);
    }

    @PostMapping("refreshToken")
    public ApiResult refreshToken(@AuthenticationPrincipal Authentication authentication) {
        final String email = authentication.getName();
        final Account account = userService.loadUserByUsername(email);
        final String token = jwtTokenProvider.createToken(email, account.getRoles());
        UserSignInResponse userSignInResponse = new UserSignInResponse(account.getEmail(), token);
        return OK(userSignInResponse);
    }
}
