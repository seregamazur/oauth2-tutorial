package com.seregamazur.oauth2.tutorial.client.model.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2AccessTokenRequest;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2TokenProvider;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2GrantType;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

@Component
public class GoogleOAuth2ClientImpl implements GoogleOAuth2Client {
    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Autowired
    private GoogleClient clientAuthorization;
    @Autowired
    private GoogleClientData clientAuthorizationData;

    @Override
    public OAuth2TokenSet convertAuthCodeToAccessToken(String authorizationCode) {
        OAuth2TokenSet accessToken = clientAuthorization.getAccessToken(
            new OAuth2AccessTokenRequest(clientId, clientSecret, authorizationCode, redirectUri, OAuth2GrantType.AUTHORIZATION_CODE.getValue()));
        accessToken.setOAuth2TokenProvider(OAuth2TokenProvider.GOOGLE);
        return accessToken;
    }

    @Override
    public GoogleUserInfo getUserInfo(String accessToken) {
        return clientAuthorizationData.getUserInfo(accessToken);
    }
}
