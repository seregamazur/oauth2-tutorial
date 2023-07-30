package com.seregamazur.oauth2.tutorial.security.jwt;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
