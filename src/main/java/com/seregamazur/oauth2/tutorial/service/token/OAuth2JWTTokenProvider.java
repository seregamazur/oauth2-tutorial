package com.seregamazur.oauth2.tutorial.service.token;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.LoginType;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2TokenProvider;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.configuration.JWTTokenConfig;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenVerificationService;
import com.seregamazur.oauth2.tutorial.verifier.oauth2.OAuth2TokenVerifier;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.seregamazur.oauth2.tutorial.utils.Constants.ACCESS_TOKEN_KEY;
import static com.seregamazur.oauth2.tutorial.utils.Constants.ACCESS_TOKEN_PROVIDER;
import static com.seregamazur.oauth2.tutorial.utils.Constants.EMAIL_KEY;
import static com.seregamazur.oauth2.tutorial.utils.Constants.LOGIN_TYPE;
import static com.seregamazur.oauth2.tutorial.utils.Constants.OAUTH2_SCOPES;
import static com.seregamazur.oauth2.tutorial.utils.Constants.OPEN_ID_TOKEN_KEY;
import static com.seregamazur.oauth2.tutorial.utils.Constants.TWO_FACTOR_ENABLED;
import static com.seregamazur.oauth2.tutorial.utils.Constants.USER_FULL_NAME_KEY;

@Service
public class OAuth2JWTTokenProvider {

    private final JWTTokenConfig tokenConfig;
    final UserRepository userRepository;
    final TokenVerificationService tokenVerificationService;

    public OAuth2JWTTokenProvider(JWTTokenConfig tokenConfig, UserRepository userRepository, TokenVerificationService tokenVerificationService) {
        this.tokenConfig = tokenConfig;
        this.userRepository = userRepository;
        this.tokenVerificationService = tokenVerificationService;
    }

    public JWTToken createJwt(OAuth2TokenSet oAuth2TokenSet) {
        OAuth2TokenProvider OAuth2TokenProvider = oAuth2TokenSet.getOAuth2TokenProvider();
        OAuth2TokenVerifier tokenVerifier = tokenVerificationService.getOAuth2TokenVerifier(OAuth2TokenProvider);
        String sub = tokenVerifier.verifyAndGetSubFromToken(oAuth2TokenSet);
        Optional<User> byEmail = userRepository.findByEmail(sub);
        User userByEmail = byEmail.orElseGet(() -> userRepository.save(new User(sub, List.of(OAuth2TokenProvider))));
        if (!userByEmail.hasProvider(OAuth2TokenProvider)) {
            userByEmail.getAuthProviders().add(OAuth2TokenProvider);
            userByEmail = userRepository.save(userByEmail);
        }

        String jwt = createTokenForOAuth2(userByEmail, oAuth2TokenSet.getAccessToken(),
            oAuth2TokenSet.getIdToken(), oAuth2TokenSet.getScope(), OAuth2TokenProvider, true);
        return new JWTToken(jwt);
    }

    private String createTokenForOAuth2(User user, String accessToken, String openid, String scopes, OAuth2TokenProvider accessTokenProvider, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity = rememberMe
            ? new Date(now + tokenConfig.getTokenValidityInMillisecondsForRememberMe())
            : new Date(now + tokenConfig.getTokenValidityInMilliseconds());

        return Jwts
            .builder()
            .setSubject(user.getId())
            .signWith(tokenConfig.getKey(), SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .claim(TWO_FACTOR_ENABLED, user.isTwoFactorEnabled())
            .claim(LOGIN_TYPE, LoginType.OAUTH2)
            .claim(ACCESS_TOKEN_PROVIDER, accessTokenProvider)
            .claim(ACCESS_TOKEN_KEY, accessToken)
            .claim(OPEN_ID_TOKEN_KEY, openid)
            .claim(OAUTH2_SCOPES, scopes)
            .claim(USER_FULL_NAME_KEY, user.getFirstName() + ", " + user.getLastName())
            .claim(EMAIL_KEY, user.getEmail())
            .compact();
    }
}
