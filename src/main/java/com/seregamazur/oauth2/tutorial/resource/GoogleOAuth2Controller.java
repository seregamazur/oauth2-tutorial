package com.seregamazur.oauth2.tutorial.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seregamazur.oauth2.tutorial.TokensCache;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClient;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClientId;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedData;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2ClientId;
import com.seregamazur.oauth2.tutorial.client.model.google.GoogleOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.google.GoogleUserInfo;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

@Controller
public class GoogleOAuth2Controller {

    @Value("${google.auth-uri}")
    private String authorizationUri;

    private final GoogleOAuth2Client googleClient;

    public GoogleOAuth2Controller(GoogleOAuth2Client googleClient) {
        this.googleClient = googleClient;
    }

    //1. UI goes to this endpoint and we redirect user to github consent
    @GetMapping("/oauth2/authorization/google")
    public String redirectToGoogleAuthorization() {
        return "redirect:" + authorizationUri;
    }

    //2. User got logged in and redirected to this endpoint with authorization code
    // We send a request with authorization code to receive access code
    @GetMapping("/oauth2/authorization/google/callback")
    public String receiveCallbackAuthorization(@RequestParam("code") String code) {
        OAuth2AccessToken oAuth2AccessToken = googleClient.convertAuthCodeToAccessToken(code);
        TokensCache.add(new OAuth2AuthorizedData(
            new OAuth2AuthorizedClientId(OAuth2ClientId.GOOGLE, "principal"),
            new OAuth2AuthorizedClient(oAuth2AccessToken, null)));
        Optional<OAuth2AuthorizedClient> authorizedClient = TokensCache.findByClientId(OAuth2ClientId.GOOGLE);
        GoogleUserInfo googleUserInfo = authorizedClient.map(client -> googleClient.getUserInfo(
            "Bearer " + client.getAccessToken().getTokenValue())).orElse(null);
//        model.addAttribute("info", googleUserInfo);
        return "App";
    }

}
