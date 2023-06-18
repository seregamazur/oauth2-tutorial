package com.seregamazur.oauth2.tutorial.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.seregamazur.oauth2.tutorial.client.model.google.GoogleOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

@Service
public class GoogleService {

    private final GoogleOAuth2Client googleClient;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public GoogleService(GoogleOAuth2Client googleClient, UserRepository userRepository,
        TokenProvider tokenProvider, GoogleIdTokenVerifier googleIdTokenVerifier) {
        this.googleClient = googleClient;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.googleIdTokenVerifier = googleIdTokenVerifier;
    }

    public JWTToken createJwtFromAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        GoogleIdToken idToken;
        try {
            idToken = googleIdTokenVerifier.verify(oAuth2AccessToken.getIdTokenValue());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        User user = userRepository.findByEmail(payload.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = tokenProvider.createToken(user, oAuth2AccessToken.getIdTokenValue(),
            oAuth2AccessToken.getScope(), "google", true);
        return new JWTToken(jwt);
    }

}
