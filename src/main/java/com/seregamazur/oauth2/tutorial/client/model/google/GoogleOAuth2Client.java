package com.seregamazur.oauth2.tutorial.client.model.google;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

public interface GoogleOAuth2Client {
    OAuth2AccessToken convertAuthCodeToAccessToken(String authorizationCode);

    GoogleUserInfo getUserInfo(String accessToken);
}
