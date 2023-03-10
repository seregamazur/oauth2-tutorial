package com.seregamazur.oauth2.tutorial.client.model.github;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

public interface GithubOAuth2Client {

    OAuth2AccessToken convertAuthCodeToAccessToken(String authorizationCode);

    GithubUserInfo getUserInfo(String accessToken);
}
