package com.seregamazur.oauth2.tutorial.client.model.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2AccessTokenRequest;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2TokenProvider;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2GrantType;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.client.model.IdToken;

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
    private GithubFeignClientAuthorization clientAuthorization;
    @Autowired
    private GithubFeignClientData githubFeignClientData;

    @Override
    public OAuth2TokenSet convertAuthCodeToAccessToken(String authorizationCode) {
        OAuth2TokenSet accessToken = clientAuthorization.getAccessToken(
            new OAuth2AccessTokenRequest(clientId, clientSecret, authorizationCode, redirectUri, OAuth2GrantType.AUTHORIZATION_CODE.getValue()));
        accessToken.setOAuth2TokenProvider(OAuth2TokenProvider.GITHUB);
        return accessToken;
    }

    @Override
    public IdToken getUserInfo(String accessToken) {
        return githubFeignClientData.verifyToken(accessToken);
    }

}
