package com.seregamazur.oauth2.tutorial.security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.service.TokenValidationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String ACCESS_TOKEN_PROVIDER = "access_token_provider";
    private static final String OAUTH2_SCOPES = "scopes";
    private static final String USER_ID_KEY = "user_id";
    private static final String USER_FULL_NAME_KEY = "user_full_name";

    @Value("${security.authentication.jwt.token-validity-in-seconds}")
    private long jwtValidityInSeconds;

    @Value("${security.authentication.jwt.token-validity-in-seconds-for-remember-me}")
    private long jwtValidityInSecondsForRememberMe;

    private final Key key;

    private final JwtParser jwtParser;

    private final long tokenValidityInMilliseconds;

    private final long tokenValidityInMillisecondsForRememberMe;
    private final TokenValidationService tokenValidationService;


    public TokenProvider(@Value("${security.authentication.jwt.base64-secret}") String jwtBase64Secret,
        TokenValidationService validationService) {
        byte[] keyBytes = new byte[0];
        if (!ObjectUtils.isEmpty(jwtBase64Secret)) {
            LOGGER.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jwtBase64Secret);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000 * jwtValidityInSeconds;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * jwtValidityInSecondsForRememberMe;
        this.tokenValidationService = validationService;
    }

    public String createToken(User user, String accessToken, String scopes, String accessTokenProvider, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts
            .builder()
            .setSubject(user.getId())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .claim(ACCESS_TOKEN_PROVIDER, accessTokenProvider)
            .claim(USER_FULL_NAME_KEY, user.getFirstName() + ", " + user.getLastName())
            .claim(ACCESS_TOKEN_KEY, accessToken)
            .claim(OAUTH2_SCOPES, scopes)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        String userId = claims.get(USER_ID_KEY, String.class);
        String fullName = claims.get(USER_FULL_NAME_KEY, String.class);
        String tokenProvider = claims.get(ACCESS_TOKEN_PROVIDER, String.class);

        UserPrincipal principal = new UserPrincipal(userId, claims.getSubject(), "", fullName, tokenProvider);

        return new UsernamePasswordAuthenticationToken(principal, token, null);
    }

    //TODO this method called in filter. need to make it general for all oauth
    public boolean validateJWTToken(String jwtToken) {
        Claims claims = jwtParser.parseClaimsJws(jwtToken).getBody();
        return tokenValidationService.verifyAndGetSubFromAccessToken((String) claims.get(ACCESS_TOKEN_KEY));
    }

}
