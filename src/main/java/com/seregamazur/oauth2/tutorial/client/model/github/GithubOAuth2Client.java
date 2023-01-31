package com.seregamazur.oauth2.tutorial.client.model.github;

import org.springframework.http.ResponseEntity;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

public interface GithubOAuth2Client {

    OAuth2AccessToken convertAuthCodeToAccessToken(String authorizationCode);

    ResponseEntity<String> getUserInfo(String accessToken);
}
