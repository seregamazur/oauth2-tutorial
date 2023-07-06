package com.seregamazur.oauth2.tutorial.service;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleService extends TokenValidationService {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleService(UserRepository userRepository,
        TokenProvider tokenProvider, GoogleIdTokenVerifier googleIdTokenVerifier) {
        super(userRepository, tokenProvider);
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    @Override
    public String verifyAndGetSubFromAccessToken(String accessToken) {
        GoogleIdToken idToken;
        try {
            idToken = googleIdTokenVerifier.verify(accessToken);
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        return idToken.getPayload().getEmail();
    }

    @Override
    public boolean verifyAccessToken(String accessToken) {
        try {
            googleIdTokenVerifier.verify(accessToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }


}
