package com.seregamazur.oauth2.tutorial.service;

import org.springframework.stereotype.Service;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OktaTokenVerifier implements TokenVerifier {

    private final AccessTokenVerifier accessTokenVerifier;

    public OktaTokenVerifier(AccessTokenVerifier accessTokenVerifier) {
        this.accessTokenVerifier = accessTokenVerifier;
    }

    @Override
    public String verifyAndGetSubFromOauthToken(OAuth2TokenSet tokenSet) {
        Jwt decode;
        try {
            decode = accessTokenVerifier.decode(tokenSet.getAccessToken());
        } catch (JwtVerificationException e) {
            throw new AccessTokenVerificationException(e);
        }
        return (String) decode.getClaims().get("sub");
    }

    @Override
    public boolean verifyOAuthToken(String token) {
        try {
            accessTokenVerifier.decode(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
