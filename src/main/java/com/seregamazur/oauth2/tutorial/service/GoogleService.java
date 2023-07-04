package com.seregamazur.oauth2.tutorial.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GoogleService extends TokenValidationService {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleService(UserRepository userRepository,
        TokenProvider tokenProvider, GoogleIdTokenVerifier googleIdTokenVerifier) {
        super(userRepository, tokenProvider);
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    @Override
    public boolean verifyAccessTokenValid(String accessToken) {
        try {
            googleIdTokenVerifier.verify(accessToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

    @Override
    public JWTToken createJwtFromAccessToken(OAuth2TokenSet oAuth2TokenSet) {
        GoogleIdToken idToken;
        try {
            idToken = googleIdTokenVerifier.verify(oAuth2TokenSet.getIdToken());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        User user = userRepository.findByEmail(payload.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = tokenProvider.createToken(user, oAuth2TokenSet.getIdToken(),
            oAuth2TokenSet.getScope(), "google", true);
        return new JWTToken(jwt);
    }

}
