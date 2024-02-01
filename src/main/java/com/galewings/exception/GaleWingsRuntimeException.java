package com.galewings.exception;

public class GaleWingsRuntimeException extends RuntimeException {
    public GaleWingsRuntimeException(Exception e) {
        super(e);
    }
}
