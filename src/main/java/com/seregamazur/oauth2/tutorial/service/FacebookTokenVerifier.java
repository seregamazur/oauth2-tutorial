package com.seregamazur.oauth2.tutorial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.IdToken;
import com.seregamazur.oauth2.tutorial.client.model.facebook.FacebookClient;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FacebookTokenVerifier implements TokenVerifier {

    private final FacebookClient facebookClient;

    @Autowired
    public FacebookTokenVerifier(FacebookClient facebookClient) {
        this.facebookClient = facebookClient;
    }

    @Override
    public String verifyAndGetSubFromOauthToken(OAuth2TokenSet tokenSet) {
        IdToken idToken;
        try {
            idToken = facebookClient.verifyToken(tokenSet.getAccessToken());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        return idToken.getEmail();
    }

    @Override
    public boolean verifyOAuthToken(String token) {
        try {
            facebookClient.verifyToken(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
