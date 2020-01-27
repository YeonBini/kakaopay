package com.kakaopay.housingfund.user.controller;

import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import com.kakaopay.housingfund.security.JwtTokenProvider;
import com.kakaopay.housingfund.user.model.Account;
import com.kakaopay.housingfund.user.model.api.request.UserJoinRequest;
import com.kakaopay.housingfund.user.model.api.request.UserLoginRequest;
import com.kakaopay.housingfund.user.model.api.response.UserSignInResponse;
import com.kakaopay.housingfund.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Api(tags = "사용자 Api")
public class UserApiController {
    private final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public UserApiController(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("signup")
    public ApiResult join(@RequestBody @ApiParam(name = "회원 가입 양식", required = true) UserJoinRequest userJoinRequest) {
        logger.info("[join]" + userJoinRequest.toString());
        Account account = new Account.Builder()
                .email(userJoinRequest.getEmail())
                .password(userJoinRequest.getPassword())
                .createdAt(LocalDateTime.now())
                .roles(userJoinRequest.getRoles())
                .build();
        userService.join(account);

        return OK(account.getEmail());
    }

    @ApiOperation(value = "로그인")
    @PostMapping("signin")
    public ApiResult login(@RequestBody @ApiParam(name = "로그인 입력 양식", required = true) UserLoginRequest userLoginRequest) {
        logger.info("[login] " + userLoginRequest.toString());
        Account account = userService.login(userLoginRequest.getEmail(), userLoginRequest.getPassword());
        final String token = jwtTokenProvider.createToken(account.getEmail(), account.getRoles());
        UserSignInResponse userSignInResponse = new UserSignInResponse(account.getEmail(), token);
        return OK(userSignInResponse);
    }

    @ApiOperation(value = "jwt 토큰 재발급")
    @ApiImplicitParam(
            name = "api_key", value = "JWT 토큰", required = true, dataType = "string",
            paramType = "header", defaultValue = "Bearer "
    )
    @PostMapping("refreshToken")
    public ApiResult refreshToken(@AuthenticationPrincipal Authentication authentication) {
        final String email = authentication.getName();
        final Account account = userService.loadUserByUsername(email);
        final String token = jwtTokenProvider.createToken(email, account.getRoles());
        UserSignInResponse userSignInResponse = new UserSignInResponse(account.getEmail(), token);
        return OK(userSignInResponse);
    }
}
