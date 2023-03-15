package com.seregamazur.oauth2.tutorial.client.model.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

@Component
public class FacebookOAuth2ClientImpl implements FacebookOAuth2Client {
    @Value("${facebook.client-id}")
    private String clientId;
    @Value("${facebook.client-secret}")
    private String clientSecret;
    @Value("${facebook.redirect-uri}")
    private String redirectUri;

    @Autowired
    private FacebookClient clientAuthorization;
    @Autowired
    private FacebookClientData clientAuthorizationData;

    @Override
    public OAuth2AccessToken convertAuthCodeToAccessToken(String authorizationCode) {
        return clientAuthorization.getAccessToken(clientId, clientSecret, redirectUri, authorizationCode);
    }

    @Override
    public FacebookUserInfo getUserInfo(String accessToken) {
        return clientAuthorizationData.getUserInfo(accessToken);
    }
}