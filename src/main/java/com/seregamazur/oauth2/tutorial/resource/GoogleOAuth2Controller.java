package com.seregamazur.oauth2.tutorial.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.seregamazur.oauth2.tutorial.client.model.google.GoogleOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.service.token.OAuth2JWTTokenProvider;

@Controller
public class GoogleOAuth2Controller {

    @Value("${google.auth-uri}")
    private String authorizationUri;

    @Value("${resource.location}")
    private String location;

    private final GoogleOAuth2Client googleClient;
    private final OAuth2JWTTokenProvider tokenCreationService;

    public GoogleOAuth2Controller(GoogleOAuth2Client googleClient, OAuth2JWTTokenProvider tokenCreationService) {
        this.googleClient = googleClient;
        this.tokenCreationService = tokenCreationService;
    }

    //1. UI goes to this endpoint and we redirect user to github consent
    @GetMapping("/oauth2/authorization/google")
    public String redirectToGoogleAuthorization() {
        return "redirect:" + authorizationUri;
    }

    //2. User got logged in and redirected to this endpoint with authorization code
    // We send a request with authorization code to receive access code
    @GetMapping("/oauth2/authorization/google/callback")
    public RedirectView receiveCallbackAuthorization(@RequestParam("code") String code) {
        OAuth2TokenSet oAuth2TokenSet = googleClient.convertAuthCodeToAccessToken(code);
        JWTToken token = tokenCreationService.createJwt(oAuth2TokenSet);
        return new RedirectView(location + "?token=" + token.getValue());
    }

}
