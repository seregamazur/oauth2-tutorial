package com.seregamazur.oauth2.tutorial.service;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleService implements TokenValidationService {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleService(GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    @Override
    public String verifyAndGetSubFromOauthToken(OAuth2TokenSet tokenSet) {
        GoogleIdToken idToken;
        try {
            idToken = googleIdTokenVerifier.verify(tokenSet.getIdToken());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        return idToken.getPayload().getEmail();
    }

    @Override
    public boolean verifyOAuthToken(String token) {
        try {
            googleIdTokenVerifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }


}
