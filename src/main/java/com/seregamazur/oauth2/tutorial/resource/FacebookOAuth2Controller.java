package com.seregamazur.oauth2.tutorial.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.seregamazur.oauth2.tutorial.client.model.facebook.FacebookOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.service.token.OAuth2JWTTokenProvider;
import com.seregamazur.oauth2.tutorial.service.UserService;

@Controller
public class FacebookOAuth2Controller {

    @Value("${facebook.auth-uri}")
    private String authorizationUri;

    @Value("${resource.location}")
    private String location;

    private final FacebookOAuth2Client metaClient;
    private final OAuth2JWTTokenProvider tokenCreationService;
    private final UserService userService;


    public FacebookOAuth2Controller(FacebookOAuth2Client metaClient, OAuth2JWTTokenProvider tokenCreationService, UserService userService) {
        this.metaClient = metaClient;
        this.tokenCreationService = tokenCreationService;
        this.userService = userService;
    }

    //1. UI goes to this endpoint and we redirect user to github consent
    @GetMapping("/oauth2/authorization/facebook")
    public String redirectToGoogleAuthorization() {
        return "redirect:" + authorizationUri;
    }

    //2. User got logged in and redirected to this endpoint with authorization code
    // We send a request with authorization code to receive access code
    @GetMapping("/oauth2/authorization/facebook/callback")
    public RedirectView receiveCallbackAuthorization(@RequestParam("code") String code) {
        OAuth2TokenSet oAuth2TokenSet = metaClient.convertAuthCodeToAccessToken(code);
        JWTToken token = tokenCreationService.createJwt(oAuth2TokenSet);
        return new RedirectView(location + "?token=" + token.getValue());
    }
}
