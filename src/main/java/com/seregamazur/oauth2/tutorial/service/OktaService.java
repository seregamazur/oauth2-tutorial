package com.seregamazur.oauth2.tutorial.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

@Service
public class OktaService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AccessTokenVerifier accessTokenVerifier;

    public OktaService(UserRepository userRepository,
        TokenProvider tokenProvider, AccessTokenVerifier accessTokenVerifier) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.accessTokenVerifier = accessTokenVerifier;
    }

    public JWTToken createJwtFromAccessToken(OAuth2TokenSet oAuth2TokenSet) {
        Jwt decode;
        try {
            decode = accessTokenVerifier.decode(oAuth2TokenSet.getAccessToken());
        } catch (JwtVerificationException e) {
            throw new AccessTokenVerificationException(e);
        }
        String sub = (String) decode.getClaims().get("sub");

        User user = userRepository.findByEmail(sub)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = tokenProvider.createToken(user, oAuth2TokenSet.getIdToken(),
            oAuth2TokenSet.getScope(), "okta", true);
        return new JWTToken(jwt);
    }
}
