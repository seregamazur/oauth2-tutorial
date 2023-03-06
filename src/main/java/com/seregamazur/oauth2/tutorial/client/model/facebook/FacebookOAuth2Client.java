package com.seregamazur.oauth2.tutorial.client.model.facebook;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

public interface FacebookOAuth2Client {
    OAuth2AccessToken convertAuthCodeToAccessToken(String authorizationCode);

    FacebookUserInfo getUserInfo(String accessToken);
}
