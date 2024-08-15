package com.example.facebook.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException() {
    }

    public EmailExistsException(String message) {
        super(message);
    }
}
