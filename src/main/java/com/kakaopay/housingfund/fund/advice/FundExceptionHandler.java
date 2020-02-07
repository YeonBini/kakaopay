package com.kakaopay.housingfund.fund.advice;

import com.kakaopay.housingfund.exception.DuplicateInstituteException;
import com.kakaopay.housingfund.exception.InstituteNotFoundException;
import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.ERROR;

@ControllerAdvice(basePackages = "com.kakaopay.housingfund.fund")
@Order(1)
public class FundExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(FundExceptionHandler.class);

    private ResponseEntity<ApiResult> newResponse(Exception e, HttpStatus httpStatus) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(e.getMessage(), HttpStatus.BAD_REQUEST), httpHeaders, httpStatus );
    }

    @ExceptionHandler(value = {
            DuplicateInstituteException.class,
            InstituteNotFoundException.class
    })
    @ResponseBody
    public ResponseEntity<ApiResult> handleFundBadRequest(Exception e) {
        logger.warn("[FundControllerAdvice] : {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

}
