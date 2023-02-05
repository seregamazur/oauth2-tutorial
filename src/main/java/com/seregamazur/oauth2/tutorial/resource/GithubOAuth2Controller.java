package com.seregamazur.oauth2.tutorial.resource;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seregamazur.oauth2.tutorial.TokensCache;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClient;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClientId;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedData;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2ClientId;
import com.seregamazur.oauth2.tutorial.client.model.github.GithubOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.github.GithubUserInfo;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

@Controller
public class GithubOAuth2Controller {

    @Value("${github.auth-uri}")
    private String authorizationUri;

    private final GithubOAuth2Client githubClient;

    public GithubOAuth2Controller(GithubOAuth2Client githubClient) {
        this.githubClient = githubClient;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String welcomePage() {
        return "index";
    }

    //1. UI goes to this endpoint and we redirect user to github consent
    @GetMapping("/oauth2/authorization/github")
    public String redirectToGithubAuthorization() {
        return "redirect:" + authorizationUri;
    }

    //2. User got logged in and redirected to this endpoint with authorization code
    // We send a request with authorization code to receive access code
    @GetMapping("/oauth2/authorization/github/callback")
    public String receiveCallbackAuthorization(@RequestParam("code") String code, Model model) {
        OAuth2AccessToken oAuth2AccessToken = githubClient.convertAuthCodeToAccessToken(code);
        TokensCache.add(new OAuth2AuthorizedData(
            new OAuth2AuthorizedClientId(OAuth2ClientId.GITHUB, "principal"),
            new OAuth2AuthorizedClient(oAuth2AccessToken, null)));
        Optional<OAuth2AuthorizedClient> authorizedClient = TokensCache.findByClientId(OAuth2ClientId.GITHUB);
        GithubUserInfo githubUserInfo = authorizedClient.map(client -> githubClient.getUserInfo(
            "Bearer " + client.getAccessToken().getTokenValue())).orElse(null);
        model.addAttribute("info", githubUserInfo);
        return "github";
    }

//    @GetMapping("/user")
//    public GithubUserInfo getUserData(@RequestParam(value = "site") OAuth2ClientId site, Model model) {
//        Optional<OAuth2AuthorizedClient> authorizedClient = TokensCache.findByClientId(site);
//        ResponseEntity<String> stringResponseEntity = authorizedClient.map(client -> githubClient.getUserInfo(
//            "Bearer " + client.getAccessToken().getTokenValue())).orElse(null);
////        model.addAttribute("", )
//        return stringResponseEntity;
//    }

    @GetMapping("/logout")
    public String logout() {
        return "index";
    }

}
