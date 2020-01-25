package com.kakaopay.housingfund.exception;

public class EmailSignFailedException extends RuntimeException{
    public EmailSignFailedException() {
        super();
    }

    public EmailSignFailedException(String message) {
        super(message);
    }

    public EmailSignFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailSignFailedException(Throwable cause) {
        super(cause);
    }

    protected EmailSignFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
