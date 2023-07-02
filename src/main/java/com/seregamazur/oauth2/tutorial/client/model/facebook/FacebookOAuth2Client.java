package com.seregamazur.oauth2.tutorial.client.model.facebook;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

public interface FacebookOAuth2Client {
    OAuth2TokenSet convertAuthCodeToAccessToken(String authorizationCode);

    FacebookUserInfo getUserInfo(String accessToken);
}
