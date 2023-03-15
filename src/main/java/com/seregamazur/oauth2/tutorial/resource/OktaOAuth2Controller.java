package com.seregamazur.oauth2.tutorial.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seregamazur.oauth2.tutorial.TokensCache;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClient;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClientId;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedData;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2ClientId;
import com.seregamazur.oauth2.tutorial.client.model.okta.OktaOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.okta.OktaUserInfo;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

@Controller
public class OktaOAuth2Controller {

    @Value("${okta.auth-uri}")
    private String authorizationUri;

    private final OktaOAuth2Client oktaClient;

    public OktaOAuth2Controller(OktaOAuth2Client oktaClient) {
        this.oktaClient = oktaClient;
    }

    //1. UI goes to this endpoint and we redirect user to github consent
    @GetMapping("/oauth2/authorization/okta")
    public String redirectToGoogleAuthorization() {
        return "redirect:" + authorizationUri;
    }

    //2. User got logged in and redirected to this endpoint with authorization code
    // We send a request with authorization code to receive access code
    @GetMapping("/oauth2/authorization/okta/callback")
    public String receiveCallbackAuthorization(@RequestParam("code") String code, Model model) {
        OAuth2AccessToken oAuth2AccessToken = oktaClient.convertAuthCodeToAccessToken(code);
        TokensCache.add(new OAuth2AuthorizedData(
            new OAuth2AuthorizedClientId(OAuth2ClientId.OKTA, "principal"),
            new OAuth2AuthorizedClient(oAuth2AccessToken, null)));
        Optional<OAuth2AuthorizedClient> authorizedClient = TokensCache.findByClientId(OAuth2ClientId.OKTA);
        OktaUserInfo googleUserInfo = authorizedClient.map(client -> oktaClient.getUserInfo(
            "Bearer " + client.getAccessToken().getTokenValue())).orElse(null);
        model.addAttribute("info", googleUserInfo);
        return "okta";
    }
}
