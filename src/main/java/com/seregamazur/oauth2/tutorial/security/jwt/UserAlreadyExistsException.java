package com.seregamazur.oauth2.tutorial.security.jwt;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
