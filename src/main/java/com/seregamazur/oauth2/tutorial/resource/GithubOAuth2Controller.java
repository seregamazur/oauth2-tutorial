package com.seregamazur.oauth2.tutorial.resource;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.seregamazur.oauth2.tutorial.client.model.github.GithubOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.service.token.OAuth2JWTTokenProvider;

@Controller
public class GithubOAuth2Controller {

    @Value("${github.auth-uri}")
    private String authorizationUri;

    @Value("${resource.location}")
    private String location;

    private final GithubOAuth2Client githubClient;
    private final OAuth2JWTTokenProvider tokenCreationService;

    public GithubOAuth2Controller(GithubOAuth2Client githubClient, OAuth2JWTTokenProvider tokenCreationService) {
        this.githubClient = githubClient;
        this.tokenCreationService = tokenCreationService;
    }

    @GetMapping("/oauth2/authorization/github")
    public String redirectToGithubAuthorization() {
        return "redirect:" + authorizationUri;
    }

    @GetMapping("/oauth2/authorization/github/callback")
    public RedirectView receiveCallbackAuthorization(@RequestParam("code") String code) {
        OAuth2TokenSet oAuth2TokenSet = githubClient.convertAuthCodeToAccessToken(code);
        JWTToken token = tokenCreationService.createJwt(oAuth2TokenSet);
        return new RedirectView(location + "?token=" + token.getValue());
    }

}
