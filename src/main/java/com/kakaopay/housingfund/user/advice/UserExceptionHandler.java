package com.kakaopay.housingfund.user.advice;

import com.kakaopay.housingfund.user.exception.EmailSignFailedException;
import com.kakaopay.housingfund.user.exception.UserNotFoundException;
import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.ERROR;

@ControllerAdvice(basePackages = "com.kakaopay.housingfund.user")
@Order(1)
public class UserExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ResponseEntity<ApiResult> newResponse(Exception e, HttpStatus httpStatus) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(e.getMessage(), HttpStatus.BAD_REQUEST), httpHeaders, httpStatus );
    }

    @ExceptionHandler(value = {
            UserNotFoundException.class
    })
    @ResponseBody
    public ResponseEntity<ApiResult> handleUserBadRequest(Exception e) {
        logger.warn("[handleUserBadRequest] : {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailSignFailedException.class)
    @ResponseBody
    public ResponseEntity<ApiResult> handleUserUnAuthorized(Exception e) {
        logger.warn("[handleUserUnAuthorized] : {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.UNAUTHORIZED);
    }

}
