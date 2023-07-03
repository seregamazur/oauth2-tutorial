package com.seregamazur.oauth2.tutorial.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.facebook.FacebookClientData;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

@Service
public class FacebookService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final FacebookClientData facebookClient;

    public FacebookService(UserRepository userRepository,
        TokenProvider tokenProvider, FacebookClientData facebookClient) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.facebookClient = facebookClient;
    }

    public JWTToken createJwtFromAccessToken(OAuth2TokenSet oAuth2TokenSet) {
        IdToken idToken;
        try {
            idToken = facebookClient.verifyToken(oAuth2TokenSet.getAccessToken());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        User user = userRepository.findByEmail(idToken.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = tokenProvider.createToken(user, oAuth2TokenSet.getAccessToken(),
            oAuth2TokenSet.getScope(), "facebook", true);
        return new JWTToken(jwt);
    }

}
