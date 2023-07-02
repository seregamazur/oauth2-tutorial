package com.seregamazur.oauth2.tutorial.client.model.okta;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

public interface OktaOAuth2Client {
    OAuth2TokenSet convertAuthCodeToAccessToken(String authorizationCode);

    OktaUserInfo getUserInfo(String accessToken);
}
