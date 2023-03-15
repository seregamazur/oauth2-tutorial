package com.seregamazur.oauth2.tutorial.client.model.okta;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

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
    @Autowired
    private OktaClientData clientAuthorizationData;

    @Override
    public OAuth2AccessToken convertAuthCodeToAccessToken(String authorizationCode) {
        return clientAuthorization.getAccessToken(Map.of("client_id", clientId, "client_secret", clientSecret, "redirect_uri", redirectUri,
            "code", authorizationCode, "grant_type", "authorization_code"));
    }

    @Override
    public OktaUserInfo getUserInfo(String accessToken) {
        return clientAuthorizationData.getUserInfo(accessToken);
    }
}
