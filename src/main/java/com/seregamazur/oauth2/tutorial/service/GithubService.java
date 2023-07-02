package com.seregamazur.oauth2.tutorial.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.client.model.github.GithubClientData;
import com.seregamazur.oauth2.tutorial.client.model.github.GithubUserInfo;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.security.jwt.AccessTokenVerificationException;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

@Service
public class GithubService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final GithubClientData githubClientData;

    public GithubService(UserRepository userRepository,
        TokenProvider tokenProvider, GithubClientData githubClientData) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.githubClientData = githubClientData;
    }

    public JWTToken createJwtFromAccessToken(OAuth2TokenSet oAuth2TokenSet) {
        GithubUserInfo userInfo;
        try {
            userInfo = githubClientData.getUserInfo("Bearer " + oAuth2TokenSet.getAccessToken());
        } catch (Exception e) {
            throw new AccessTokenVerificationException(e);
        }
        User user = userRepository.findByEmail(userInfo.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwt = tokenProvider.createToken(user, oAuth2TokenSet.getIdToken(),
            oAuth2TokenSet.getScope(), "github", true);
        return new JWTToken(jwt);
    }

}
