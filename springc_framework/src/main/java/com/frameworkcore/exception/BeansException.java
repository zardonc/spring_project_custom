package com.frameworkcore.exception;

public class BeansException extends RuntimeException {
    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }
}
