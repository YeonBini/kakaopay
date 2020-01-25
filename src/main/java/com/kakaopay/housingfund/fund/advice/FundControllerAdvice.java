package com.kakaopay.housingfund.fund.advice;

import com.kakaopay.housingfund.exception.DuplicateInstituteException;
import com.kakaopay.housingfund.exception.InstituteNotFoundException;
import com.kakaopay.housingfund.fund.model.Institute;
import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.ERROR;

@ControllerAdvice(basePackages = "com.kakaopay.housingfund.fund")
public class FundControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(FundControllerAdvice.class);

    @ExceptionHandler(value = {
            NullPointerException.class,
            IllegalArgumentException.class,
            DuplicateInstituteException.class,
            InstituteNotFoundException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResult fundHandler(Exception e) {
        logger.error("[FundControllerAdvice] " + e);
        return ERROR(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
