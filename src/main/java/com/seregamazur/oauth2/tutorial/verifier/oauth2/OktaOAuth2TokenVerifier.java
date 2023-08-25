package com.seregamazur.oauth2.tutorial.verifier.oauth2;

import org.springframework.stereotype.Service;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OktaOAuth2TokenVerifier implements OAuth2TokenVerifier {

    private final AccessTokenVerifier accessTokenVerifier;

    public OktaOAuth2TokenVerifier(AccessTokenVerifier accessTokenVerifier) {
        this.accessTokenVerifier = accessTokenVerifier;
    }

    @Override
    public String verifyAndGetSubFromToken(OAuth2TokenSet tokenSet) {
        Jwt decode;
        try {
            decode = accessTokenVerifier.decode(tokenSet.getAccessToken());
        } catch (JwtVerificationException e) {
            throw new AccessTokenVerificationException(e);
        }
        return (String) decode.getClaims().get("sub");
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            accessTokenVerifier.decode(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
