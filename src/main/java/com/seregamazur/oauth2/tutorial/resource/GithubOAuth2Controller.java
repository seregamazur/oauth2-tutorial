package com.seregamazur.oauth2.tutorial.resource;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seregamazur.oauth2.tutorial.TokensCache;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClient;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClientId;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedData;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2ClientId;
import com.seregamazur.oauth2.tutorial.client.model.github.GithubClientData;
import com.seregamazur.oauth2.tutorial.client.model.github.GithubOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

@Controller
public class GithubOAuth2Controller {

    @Value("${github.auth-uri}")
    private String authorizationUri;

    private final GithubOAuth2Client githubClient;

    public GithubOAuth2Controller(GithubOAuth2Client githubClient) {
        this.githubClient = githubClient;
    }

    //1. UI goes to this endpoint and we redirect user to github consent
    @GetMapping("/oauth2/authorization/github")
    public void redirectToGithubAuthorization(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", authorizationUri);
        httpServletResponse.setStatus(302);
    }

    //2. User got logged in and redirected to this endpoint with authorization code
    // We send a request with authorization code to receive access code
    @GetMapping("/oauth2/authorization/github/callback")
    public void receiveCallbackAuthorization(@RequestParam("code") String code, HttpServletResponse httpServletResponse) {
        OAuth2AccessToken oAuth2AccessToken = githubClient.convertAuthCodeToAccessToken(code);
        TokensCache.add(new OAuth2AuthorizedData(
            new OAuth2AuthorizedClientId(OAuth2ClientId.GITHUB, "principal"),
            new OAuth2AuthorizedClient(oAuth2AccessToken, null)));
        httpServletResponse.setHeader("Location", "http://localhost:8081/info.html");
        httpServletResponse.setHeader("Set-Cookie", "site=github");
        httpServletResponse.setStatus(302);
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUserData(@RequestParam(value = "site") String site) {
        Optional<OAuth2AuthorizedClient> authorizedClient = TokensCache.findByClientId(site);
        return authorizedClient.map(client -> githubClient.getUserInfo(
            "Bearer " + client.getAccessToken().getTokenValue())).orElse(null);
    }

    @PostMapping("/logout")
    public void logout() {

    }

}
