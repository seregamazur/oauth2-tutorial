package com.seregamazur.oauth2.tutorial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.IdToken;
import com.seregamazur.oauth2.tutorial.client.model.facebook.FacebookClient;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FacebookService extends TokenValidationService {

    private final FacebookClient facebookClient;

    @Autowired
    public FacebookService(UserRepository userRepository, TokenProvider tokenProvider,
        FacebookClient facebookClient) {
        super(userRepository, tokenProvider);
        this.facebookClient = facebookClient;
    }

    @Override
    public String verifyAndGetSubFromAccessToken(String accessToken) {
        IdToken idToken;
        try {
            idToken = facebookClient.verifyToken(accessToken);
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        return idToken.getEmail();
    }

    @Override
    public boolean verifyAccessToken(String accessToken) {
        try {
            facebookClient.verifyToken(accessToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
