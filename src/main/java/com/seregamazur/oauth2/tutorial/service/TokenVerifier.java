package com.seregamazur.oauth2.tutorial.service;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

public interface TokenVerifier {

    boolean verifyOAuthToken(String token);

    String verifyAndGetSubFromOauthToken(OAuth2TokenSet tokenSet);

}
