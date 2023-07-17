package com.seregamazur.oauth2.tutorial.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

@Component
public class JWTTokenCreationService {

    final UserRepository userRepository;
    final TokenProvider tokenProvider;

    public JWTTokenCreationService(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public JWTToken createJwt(User user, boolean rememberMe) {
        String jwt = tokenProvider.createToken(user, rememberMe);
        return new JWTToken(jwt);
    }

    public JWTToken createJwtFromAccessToken(OAuth2TokenSet oAuth2TokenSet) {
        TokenVerifier tokenVerifier = tokenProvider.getAccessTokenVerifier(oAuth2TokenSet.getAccessTokenProvider());
        String sub = tokenVerifier.verifyAndGetSubFromOauthToken(oAuth2TokenSet);
        User user = userRepository.findByEmail(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = tokenProvider.createTokenForOAuth2(user, oAuth2TokenSet.getIdToken(),
            oAuth2TokenSet.getScope(), oAuth2TokenSet.getAccessTokenProvider(), true);
        return new JWTToken(jwt);
    }


}
