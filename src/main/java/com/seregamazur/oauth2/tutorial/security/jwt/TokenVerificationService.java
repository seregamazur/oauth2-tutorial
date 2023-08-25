package com.seregamazur.oauth2.tutorial.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.LoginType;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2TokenProvider;
import com.seregamazur.oauth2.tutorial.configuration.JWTTokenConfig;
import com.seregamazur.oauth2.tutorial.verifier.credentials.CredentialsTokenVerifier;
import com.seregamazur.oauth2.tutorial.verifier.oauth2.FacebookOAuth2TokenVerifier;
import com.seregamazur.oauth2.tutorial.verifier.oauth2.GithubOAuth2TokenVerifier;
import com.seregamazur.oauth2.tutorial.verifier.oauth2.GoogleOAuth2TokenVerifier;
import com.seregamazur.oauth2.tutorial.verifier.oauth2.OAuth2TokenVerifier;
import com.seregamazur.oauth2.tutorial.verifier.oauth2.OktaOAuth2TokenVerifier;

import io.jsonwebtoken.Claims;

import static com.seregamazur.oauth2.tutorial.utils.Constants.ACCESS_TOKEN_PROVIDER;
import static com.seregamazur.oauth2.tutorial.utils.Constants.EMAIL_KEY;
import static com.seregamazur.oauth2.tutorial.utils.Constants.LOGIN_TYPE;
import static com.seregamazur.oauth2.tutorial.utils.Constants.OPEN_ID_TOKEN_KEY;
import static com.seregamazur.oauth2.tutorial.utils.Constants.USER_FULL_NAME_KEY;

@Component
public class TokenVerificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenVerificationService.class);

    private final GoogleOAuth2TokenVerifier googleTokenValidator;
    private final GithubOAuth2TokenVerifier githubTokenValidator;
    private final FacebookOAuth2TokenVerifier facebookTokenValidator;
    private final OktaOAuth2TokenVerifier oktaTokenValidator;
    private final CredentialsTokenVerifier credentialsTokenVerifier;

    private final JWTTokenConfig tokenConfig;

    public TokenVerificationService(
        JWTTokenConfig tokenConfig,
        GoogleOAuth2TokenVerifier googleTokenValidator, GithubOAuth2TokenVerifier githubTokenValidator,
        FacebookOAuth2TokenVerifier facebookTokenValidator, OktaOAuth2TokenVerifier oktaTokenValidator,
        CredentialsTokenVerifier credentialsTokenVerifier) {
        this.tokenConfig = tokenConfig;
        this.googleTokenValidator = googleTokenValidator;
        this.githubTokenValidator = githubTokenValidator;
        this.facebookTokenValidator = facebookTokenValidator;
        this.oktaTokenValidator = oktaTokenValidator;
        this.credentialsTokenVerifier = credentialsTokenVerifier;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = tokenConfig.getJwtParser().parseClaimsJws(token).getBody();

        String fullName = claims.get(USER_FULL_NAME_KEY, String.class);
        String loginType = claims.get(LOGIN_TYPE, String.class);
//        String tokenProvider = claims.get(ACCESS_TOKEN_PROVIDER, String.class);
        String email = claims.get(EMAIL_KEY, String.class);

        UserPrincipal principal = new UserPrincipal(claims.getSubject(), email, "", fullName, loginType);

        return new UsernamePasswordAuthenticationToken(principal, token, null);
    }

    public boolean validateJWTToken(String jwtToken) {
        Claims claims = tokenConfig.getJwtParser().parseClaimsJws(jwtToken).getBody();
        LoginType loginType = LoginType.valueOf((String) claims.get(LOGIN_TYPE));
        if (loginType == LoginType.OAUTH2) {
            OAuth2TokenProvider issuer = OAuth2TokenProvider.valueOf(claims.get(ACCESS_TOKEN_PROVIDER, String.class));
            OAuth2TokenVerifier verifier = getAccessTokenVerifier(issuer);
            String accessToken = (String) claims.get(OPEN_ID_TOKEN_KEY);
            return verifier.verifyToken(accessToken);
        } else {
            return credentialsTokenVerifier.verifyCredentialsToken(jwtToken);
        }
    }

    public OAuth2TokenVerifier getAccessTokenVerifier(OAuth2TokenProvider clientId) {
        switch (clientId) {
            case GOOGLE:
                return googleTokenValidator;
            case FACEBOOK:
                return facebookTokenValidator;
            case OKTA:
                return oktaTokenValidator;
            case GITHUB:
                return githubTokenValidator;
            default:
                throw new RuntimeException("Unknown OAuth2 Token provider");
        }
    }

}
