package com.seregamazur.oauth2.tutorial.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.User;
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

    public JWTToken createJwtFromAccessToken(OAuth2TokenSet oAuth2TokenSet) {
        String sub = verifyAndGetSubAccessToken(oAuth2TokenSet.getAccessToken());
        User user = userRepository.findByEmail(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = tokenProvider.createToken(user, oAuth2TokenSet.getIdToken(),
            oAuth2TokenSet.getScope(), "okta", true);
        return new JWTToken(jwt);
    }

    public abstract String verifyAndGetSubAccessToken(String accessToken);

    public abstract boolean verifyAccessToken(String accessToken);

}
