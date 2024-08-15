package com.example.facebook.exception;

public class CountryExistsException extends RuntimeException {
    public CountryExistsException() {
    }

    public CountryExistsException(String message) {
        super(message);
    }
}
