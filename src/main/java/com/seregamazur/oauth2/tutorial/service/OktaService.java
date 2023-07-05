package com.seregamazur.oauth2.tutorial.service;

import org.springframework.stereotype.Service;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
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
    public String verifyAndGetSubAccessToken(String accessToken) {
        Jwt decode;
        try {
            decode = accessTokenVerifier.decode(accessToken);
        } catch (JwtVerificationException e) {
            throw new AccessTokenVerificationException(e);
        }
        return (String) decode.getClaims().get("sub");
    }

    @Override
    public boolean verifyAccessToken(String accessToken) {
        try {
            accessTokenVerifier.decode(accessToken);
            return true;
        } catch (Exception e) {
            log.error("Invalid access_token.", e);
        }
        return false;
    }

}
