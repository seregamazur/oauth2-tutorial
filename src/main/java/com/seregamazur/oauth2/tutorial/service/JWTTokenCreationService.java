package com.seregamazur.oauth2.tutorial.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.seregamazur.oauth2.tutorial.client.model.LoginProvider;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserDTO;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

@Component
public class JWTTokenCreationService {

    final UserRepository userRepository;
    final TokenProvider tokenProvider;

    public JWTTokenCreationService(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public JWTToken createJwt(UserDTO user, boolean rememberMe) {
        String jwt = tokenProvider.createToken(user, rememberMe);
        return new JWTToken(jwt);
    }

    public JWTToken createJwtFromAccessToken(OAuth2TokenSet oAuth2TokenSet) {
        LoginProvider loginProvider = oAuth2TokenSet.getLoginProvider();
        TokenVerifier tokenVerifier = tokenProvider.getAccessTokenVerifier(loginProvider);
        String sub = tokenVerifier.verifyAndGetSubFromOauthToken(oAuth2TokenSet);
        Optional<User> byEmail = userRepository.findByEmail(sub);
        User userByEmail = byEmail.orElseGet(() -> userRepository.save(new User(sub, List.of(loginProvider))));
        if (!userByEmail.hasProvider(loginProvider)) {
            userByEmail.getAuthProviders().add(loginProvider);
            userByEmail = userRepository.save(userByEmail);
        }

        String jwt = tokenProvider.createTokenForOAuth2(userByEmail, oAuth2TokenSet.getAccessToken(),
            oAuth2TokenSet.getScope(), loginProvider, true);
        return new JWTToken(jwt);
    }


}
