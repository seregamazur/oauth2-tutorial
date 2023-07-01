package com.seregamazur.oauth2.tutorial.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

@Service
public class FacebookService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final FacebookIdTokenVerifier facebookIdTokenVerifier;

    public FacebookService(UserRepository userRepository,
        TokenProvider tokenProvider, FacebookIdTokenVerifier facebookIdTokenVerifier) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.facebookIdTokenVerifier = facebookIdTokenVerifier;
    }

    public JWTToken createJwtFromAccessToken(OAuth2AccessToken oAuth2AccessToken) {
//        verifyToken(oAuth2AccessToken.getIdTokenValue());
        FacebookDecodedPayload facebookDecodedPayload;
        try {
            facebookDecodedPayload = facebookIdTokenVerifier.verifyToken(oAuth2AccessToken.getIdTokenValue());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        User user = userRepository.findByEmail(facebookDecodedPayload.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = tokenProvider.createToken(user, oAuth2AccessToken.getIdTokenValue(),
            oAuth2AccessToken.getScope(), "facebook", true);
        return new JWTToken(jwt);
    }

}
