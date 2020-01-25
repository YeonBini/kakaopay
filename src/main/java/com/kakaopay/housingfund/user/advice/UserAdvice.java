package com.kakaopay.housingfund.user.advice;

import com.kakaopay.housingfund.exception.EmailSignFailedException;
import com.kakaopay.housingfund.exception.UserNotFoundException;
import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.kakaopay.housingfund.user")
public class UserAdvice {

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            EmailSignFailedException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResult accountAdviceHandler(Exception e) {
        return ApiResult.ERROR(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
