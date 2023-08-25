package com.seregamazur.oauth2.tutorial.verifier.oauth2;

import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.IdToken;
import com.seregamazur.oauth2.tutorial.client.model.github.GithubClientData;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GithubOAuth2TokenVerifier implements OAuth2TokenVerifier {

    private final GithubClientData githubClientData;

    public GithubOAuth2TokenVerifier(GithubClientData githubClientData) {
        this.githubClientData = githubClientData;
    }

    @Override
    public String verifyAndGetSubFromToken(OAuth2TokenSet tokenSet) {
        IdToken idToken;
        try {
            idToken = githubClientData.verifyToken("Bearer " + tokenSet.getAccessToken());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        return idToken.getEmail();
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            githubClientData.verifyToken(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
