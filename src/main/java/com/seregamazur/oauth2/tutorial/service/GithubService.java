package com.seregamazur.oauth2.tutorial.service;

import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.IdToken;
import com.seregamazur.oauth2.tutorial.client.model.github.GithubClientData;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GithubService extends TokenValidationService {

    private final GithubClientData githubClientData;

    public GithubService(UserRepository userRepository,
        TokenProvider tokenProvider, GithubClientData githubClientData) {
        super(userRepository, tokenProvider);
        this.githubClientData = githubClientData;
    }

    @Override
    public String verifyAndGetSubFromAccessToken(String accessToken) {
        IdToken idToken;
        try {
            idToken = githubClientData.verifyToken("Bearer " + accessToken);
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        return idToken.getEmail();
    }

    @Override
    public boolean verifyAccessToken(String accessToken) {
        try {
            githubClientData.verifyToken(accessToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
