package com.galewings.exception;

public class GaleWingsSystemException extends RuntimeException {

    public GaleWingsSystemException() {
    }

    public GaleWingsSystemException(InterruptedException e) {
        super(e);
    }

    public GaleWingsSystemException(String message) {
        super(message);
    }

}
