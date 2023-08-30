package com.seregamazur.oauth2.tutorial.configuration;

import java.security.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.seregamazur.oauth2.tutorial.security.jwt.TokenVerificationService;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTokenConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenVerificationService.class);

    private final Key key;
    private final JwtParser jwtParser;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    public JWTTokenConfig(
        @Value("${security.authentication.jwt.base64-secret}") String jwtBase64Secret,
        @Value("${security.authentication.jwt.token-validity-in-seconds}") long jwtValidityInSeconds,
        @Value("${security.authentication.jwt.token-validity-in-seconds-for-remember-me}") long jwtValidityInSecondsForRememberMe
    ) {
        byte[] keyBytes = new byte[0];
        if (!ObjectUtils.isEmpty(jwtBase64Secret)) {
            LOGGER.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jwtBase64Secret);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000 * jwtValidityInSeconds;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * jwtValidityInSecondsForRememberMe;
    }

    public Key getKey() {
        return key;
    }

    public JwtParser getJwtParser() {
        return jwtParser;
    }

    public long getTokenValidityInMilliseconds() {
        return tokenValidityInMilliseconds;
    }

    public long getTokenValidityInMillisecondsForRememberMe() {
        return tokenValidityInMillisecondsForRememberMe;
    }
}

