package com.kakaopay.housingfund.exception;

public class InstituteNotFoundException extends RuntimeException {
    public InstituteNotFoundException() {
        super();
    }

    public InstituteNotFoundException(String message) {
        super(message);
    }

    public InstituteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstituteNotFoundException(Throwable cause) {
        super(cause);
    }

    protected InstituteNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
