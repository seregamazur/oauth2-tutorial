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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OktaService extends TokenValidationService {

    private final AccessTokenVerifier accessTokenVerifier;

    public OktaService(UserRepository userRepository,
        TokenProvider tokenProvider, AccessTokenVerifier accessTokenVerifier) {
        super(userRepository, tokenProvider);
        this.accessTokenVerifier = accessTokenVerifier;
    }

    @Override
    public boolean verifyAccessTokenValid(String accessToken) {
        try {
            accessTokenVerifier.decode(accessToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

    @Override
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
