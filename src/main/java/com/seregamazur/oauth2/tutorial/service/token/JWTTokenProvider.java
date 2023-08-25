package com.seregamazur.oauth2.tutorial.service.token;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.LoginType;
import com.seregamazur.oauth2.tutorial.configuration.JWTTokenConfig;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserDTO;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenVerificationService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static com.seregamazur.oauth2.tutorial.utils.Constants.EMAIL_KEY;
import static com.seregamazur.oauth2.tutorial.utils.Constants.LOGIN_TYPE;
import static com.seregamazur.oauth2.tutorial.utils.Constants.USER_FULL_NAME_KEY;

@Service
public class JWTTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenVerificationService.class);

    final JWTTokenConfig tokenConfig;
    final UserRepository userRepository;
    final TokenVerificationService tokenVerificationService;

    public JWTTokenProvider(UserRepository userRepository, TokenVerificationService tokenVerificationService,
        JWTTokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
        this.userRepository = userRepository;
        this.tokenVerificationService = tokenVerificationService;
    }

    public JWTToken createJwt(User user, boolean rememberMe) {
        LOGGER.info("Creating JWT token for credentials: {}", user);
        String jwt = createTokenForCreds(user, rememberMe);
        return new JWTToken(jwt);
    }

    private String createTokenForCreds(User user, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity = rememberMe
            ? new Date(now + tokenConfig.getTokenValidityInMillisecondsForRememberMe())
            : new Date(now + tokenConfig.getTokenValidityInMilliseconds());

        return Jwts
            .builder()
            .setSubject(user.getId())
            .signWith(tokenConfig.getKey(), SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .claim(LOGIN_TYPE, LoginType.CREDENTIALS)
            .claim(USER_FULL_NAME_KEY, user.getFirstName() + ", " + user.getLastName())
            .claim(EMAIL_KEY, user.getEmail())
            .compact();
    }

}
