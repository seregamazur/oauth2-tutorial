package com.seregamazur.oauth2.tutorial.client.model.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2TokenProvider;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

@Component
public class FacebookOAuth2ClientImpl implements FacebookOAuth2Client {
    @Value("${facebook.client-id}")
    private String clientId;
    @Value("${facebook.client-secret}")
    private String clientSecret;
    @Value("${facebook.redirect-uri}")
    private String redirectUri;

    @Autowired
    private FacebookFeignClient clientAuthorization;

    @Override
    public OAuth2TokenSet convertAuthCodeToAccessToken(String authorizationCode) {
        OAuth2TokenSet accessToken = clientAuthorization.getAccessToken(clientId, clientSecret, redirectUri, authorizationCode);
        accessToken.setOAuth2TokenProvider(OAuth2TokenProvider.FACEBOOK);
        return accessToken;
    }

    @Override
    public FacebookUserInfo getUserInfo(String accessToken) {
        return clientAuthorization.getUserInfo(accessToken);
    }
}
