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

import com.seregamazur.oauth2.tutorial.client.model.OAuth2ClientId;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserDTO;
import com.seregamazur.oauth2.tutorial.service.FacebookTokenVerifier;
import com.seregamazur.oauth2.tutorial.service.GithubTokenVerifier;
import com.seregamazur.oauth2.tutorial.service.GoogleTokenVerifier;
import com.seregamazur.oauth2.tutorial.service.OktaTokenVerifier;
import com.seregamazur.oauth2.tutorial.service.TokenVerifier;

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
    private final GoogleTokenVerifier googleTokenValidator;
    private final GithubTokenVerifier githubTokenValidator;
    private final FacebookTokenVerifier facebookTokenValidator;
    private final OktaTokenVerifier oktaTokenValidator;


    public TokenProvider(@Value("${security.authentication.jwt.base64-secret}") String jwtBase64Secret,
        GoogleTokenVerifier googleTokenValidator, GithubTokenVerifier githubTokenValidator, FacebookTokenVerifier facebookTokenValidator, OktaTokenVerifier oktaTokenValidator) {
        byte[] keyBytes = new byte[0];
        if (!ObjectUtils.isEmpty(jwtBase64Secret)) {
            LOGGER.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(jwtBase64Secret);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000 * jwtValidityInSeconds;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * jwtValidityInSecondsForRememberMe;
        this.googleTokenValidator = googleTokenValidator;
        this.githubTokenValidator = githubTokenValidator;
        this.facebookTokenValidator = facebookTokenValidator;
        this.oktaTokenValidator = oktaTokenValidator;
    }

    public String createTokenForOAuth2(User user, String accessToken, String scopes, OAuth2ClientId accessTokenProvider, boolean rememberMe) {
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

    public String createToken(UserDTO user, boolean rememberMe) {
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
            .claim(ACCESS_TOKEN_PROVIDER, "INTERNAL")
            .claim(USER_FULL_NAME_KEY, user.getFirstName() + ", " + user.getLastName())
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

    public boolean validateJWTToken(String jwtToken) {
        Claims claims = jwtParser.parseClaimsJws(jwtToken).getBody();
        String accessToken = (String) claims.get(ACCESS_TOKEN_KEY);
        OAuth2ClientId issuer = (OAuth2ClientId) claims.get(ACCESS_TOKEN_PROVIDER);
        TokenVerifier verifier = getAccessTokenVerifier(issuer);
        return verifier.verifyOAuthToken(accessToken);
    }

    public TokenVerifier getAccessTokenVerifier(OAuth2ClientId clientId) {
        switch (clientId) {
            case GOOGLE:
                return googleTokenValidator;
            case FACEBOOK:
                return facebookTokenValidator;
            case OKTA:
                return oktaTokenValidator;
            default:
                return githubTokenValidator;
        }
    }

}
