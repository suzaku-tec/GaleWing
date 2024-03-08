package com.galewings.exception;

public class GaleWingsSystemException extends RuntimeException {

    public GaleWingsSystemException(InterruptedException e) {
        super(e);
    }
}
