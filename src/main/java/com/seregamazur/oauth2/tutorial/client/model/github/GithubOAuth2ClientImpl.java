package com.seregamazur.oauth2.tutorial.client.model.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2AccessTokenRequest;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GithubOAuth2ClientImpl implements GithubOAuth2Client {

    @Value("${github.client-id}")
    private String clientId;
    @Value("${github.client-secret}")
    private String clientSecret;
    @Value("${github.redirect-uri}")
    private String redirectUri;

    @Autowired
    private GithubClientAuthorization clientAuthorization;
    @Autowired
    private GithubClientData githubClientData;

    @Override
    public OAuth2AccessToken convertAuthCodeToAccessToken(String authorizationCode) {
        return clientAuthorization.getAccessToken(
            new OAuth2AccessTokenRequest(clientId, clientSecret, authorizationCode, redirectUri));
    }

    @Override
    public GithubUserInfo getUserInfo(String accessToken) {
        return githubClientData.getUserInfo(accessToken);
    }

}
