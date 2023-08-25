package com.seregamazur.oauth2.tutorial.verifier.credentials;

import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.configuration.JWTTokenConfig;

import io.jsonwebtoken.JwtException;

@Service
public class CredentialsTokenVerifier {

    private final JWTTokenConfig tokenConfig;

    public CredentialsTokenVerifier(JWTTokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    public boolean verifyCredentialsToken(String jwtToken) {
        try {
            tokenConfig.getJwtParser()
                .parseClaimsJws(jwtToken)
                .getBody();
            return true;
        } catch (JwtException ex) {
            throw new JwtException("Invalid token: " + ex.getMessage());
        }
    }
}
