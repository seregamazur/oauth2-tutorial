package com.seregamazur.oauth2.tutorial.client.model.okta;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2ClientId;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

@Component
public class OktaOAuth2ClientImpl implements OktaOAuth2Client {
    @Value("${okta.client-id}")
    private String clientId;
    @Value("${okta.client-secret}")
    private String clientSecret;
    @Value("${okta.redirect-uri}")
    private String redirectUri;

    @Autowired
    private OktaClient clientAuthorization;

    @Override
    public OAuth2TokenSet convertAuthCodeToAccessToken(String authorizationCode) {
        OAuth2TokenSet accessToken = clientAuthorization.getAccessToken(Map.of("client_id", clientId, "client_secret", clientSecret, "redirect_uri", redirectUri,
            "code", authorizationCode, "grant_type", "authorization_code"));
        accessToken.setAccessTokenProvider(OAuth2ClientId.OKTA);
        return accessToken;
    }

    @Override
    public OktaUserInfo getUserInfo(String accessToken) {
        return clientAuthorization.getUserInfo(accessToken);
    }
}
