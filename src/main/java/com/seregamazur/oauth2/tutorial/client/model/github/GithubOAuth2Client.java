package com.seregamazur.oauth2.tutorial.client.model.github;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.client.model.IdToken;

public interface GithubOAuth2Client {

    OAuth2TokenSet convertAuthCodeToAccessToken(String authorizationCode);

    IdToken getUserInfo(String accessToken);
}
