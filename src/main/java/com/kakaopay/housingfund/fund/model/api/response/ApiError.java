package com.kakaopay.housingfund.fund.model.api.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;

public class ApiError {
    private final HttpStatus httpStatus;
    private final String errorMessage;

    public ApiError(Throwable throwable, HttpStatus httpStatus) {
        this(throwable.getMessage(), httpStatus);
    }

    public ApiError(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("httpStatus", httpStatus)
                .append("errorMessage", errorMessage)
                .toString();
    }
}
