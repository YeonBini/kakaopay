package com.kakaopay.housingfund.exception.handler;

import com.kakaopay.housingfund.fund.model.api.response.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.kakaopay.housingfund.fund.model.api.response.ApiResult.ERROR;

@ControllerAdvice
@Order
public class GeneralExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseBody
    public ApiResult exceptionHandler(Exception e) {
        logger.warn("[exceptionHandler] {}", e.getMessage(), e);
        return ERROR(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
