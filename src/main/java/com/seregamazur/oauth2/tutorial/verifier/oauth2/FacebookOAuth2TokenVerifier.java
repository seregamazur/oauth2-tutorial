package com.seregamazur.oauth2.tutorial.verifier.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.IdToken;
import com.seregamazur.oauth2.tutorial.client.model.facebook.FacebookFeignClient;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FacebookOAuth2TokenVerifier implements OAuth2TokenVerifier {

    private final FacebookFeignClient facebookFeignClient;

    @Autowired
    public FacebookOAuth2TokenVerifier(FacebookFeignClient facebookFeignClient) {
        this.facebookFeignClient = facebookFeignClient;
    }

    @Override
    public String verifyAndGetSubFromToken(OAuth2TokenSet tokenSet) {
        IdToken idToken;
        try {
            idToken = facebookFeignClient.verifyToken(tokenSet.getAccessToken());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        return idToken.getEmail();
    }

    @Override
    public boolean verifyToken(OAuth2TokenPair token) {
        try {
            facebookFeignClient.verifyToken(token.getAccessToken());
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
