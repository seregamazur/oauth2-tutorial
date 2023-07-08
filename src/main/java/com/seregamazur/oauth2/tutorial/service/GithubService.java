package com.seregamazur.oauth2.tutorial.service;

import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.IdToken;
import com.seregamazur.oauth2.tutorial.client.model.github.GithubClientData;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GithubService implements TokenValidationService {

    private final GithubClientData githubClientData;

    public GithubService(GithubClientData githubClientData) {
        this.githubClientData = githubClientData;
    }

    @Override
    public String verifyAndGetSubFromOauthToken(OAuth2TokenSet tokenSet) {
        IdToken idToken;
        try {
            idToken = githubClientData.verifyToken("Bearer " + tokenSet.getAccessToken());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        return idToken.getEmail();
    }

    @Override
    public boolean verifyOAuthToken(String token) {
        try {
            githubClientData.verifyToken(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
