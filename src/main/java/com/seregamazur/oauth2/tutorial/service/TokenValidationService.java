package com.seregamazur.oauth2.tutorial.service;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

public abstract class TokenValidationService {

    final UserRepository userRepository;
    final TokenProvider tokenProvider;

    public TokenValidationService(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public abstract boolean verifyAccessTokenValid(String accessToken);

    public abstract JWTToken createJwtFromAccessToken(OAuth2TokenSet oAuth2TokenSet);
}
