package com.kakaopay.housingfund.exception;

public class DuplicateInstituteException extends RuntimeException {
    public DuplicateInstituteException() {
        super();
    }

    public DuplicateInstituteException(String message) {
        super(message);
    }

    public DuplicateInstituteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateInstituteException(Throwable cause) {
        super(cause);
    }

    protected DuplicateInstituteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
