package com.ninjaone.backendinterviewproject.exception;

public class GenericApiException extends RuntimeException {
    public GenericApiException(String message, Exception ex) {
        super(message, ex);
    }

    public GenericApiException(String message) {
        super(message);
    }
}
