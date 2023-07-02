package com.seregamazur.oauth2.tutorial.client.model.google;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

public interface GoogleOAuth2Client {
    OAuth2TokenSet convertAuthCodeToAccessToken(String authorizationCode);

    GoogleUserInfo getUserInfo(String accessToken);
}
