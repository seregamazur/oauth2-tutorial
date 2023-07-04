package com.seregamazur.oauth2.tutorial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.IdToken;
import com.seregamazur.oauth2.tutorial.client.model.facebook.FacebookClient;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FacebookService extends TokenValidationService {

    private final FacebookClient facebookClient;

    @Autowired
    public FacebookService(UserRepository userRepository, TokenProvider tokenProvider,
        FacebookClient facebookClient) {
        super(userRepository, tokenProvider);
        this.facebookClient = facebookClient;
    }

    @Override
    public boolean verifyAccessTokenValid(String accessToken) {
        try {
            facebookClient.verifyToken(accessToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

    @Override
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
