package com.seregamazur.oauth2.tutorial.client.model.okta;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

public interface OktaOAuth2Client {
    OAuth2AccessToken convertAuthCodeToAccessToken(String authorizationCode);

    OktaUserInfo getUserInfo(String accessToken);
}
