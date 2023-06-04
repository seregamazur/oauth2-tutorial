package com.seregamazur.oauth2.tutorial.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.seregamazur.oauth2.tutorial.TokensCache;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClient;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClientId;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedData;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2ClientId;
import com.seregamazur.oauth2.tutorial.client.model.facebook.FacebookOAuth2Client;
import com.seregamazur.oauth2.tutorial.client.model.facebook.FacebookUserInfo;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

@Controller
public class FacebookOAuth2Controller {

    @Value("${facebook.auth-uri}")
    private String authorizationUri;

    private final FacebookOAuth2Client metaClient;

    public FacebookOAuth2Controller(FacebookOAuth2Client googleClient) {
        this.metaClient = googleClient;
    }

    //1. UI goes to this endpoint and we redirect user to github consent
    @GetMapping("/oauth2/authorization/facebook")
    public String redirectToGoogleAuthorization(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("redirectUrl", authorizationUri);
        return "redirect:" + authorizationUri;
    }

    //2. User got logged in and redirected to this endpoint with authorization code
    // We send a request with authorization code to receive access code
    @GetMapping("/oauth2/authorization/facebook/callback")
    public String receiveCallbackAuthorization(@RequestParam("code") String code, Model model) {
        OAuth2AccessToken oAuth2AccessToken = metaClient.convertAuthCodeToAccessToken(code);
        TokensCache.add(new OAuth2AuthorizedData(
            new OAuth2AuthorizedClientId(OAuth2ClientId.FACEBOOK, "principal"),
            new OAuth2AuthorizedClient(oAuth2AccessToken, null)));
        Optional<OAuth2AuthorizedClient> authorizedClient = TokensCache.findByClientId(OAuth2ClientId.FACEBOOK);
        FacebookUserInfo facebookUserInfo = authorizedClient.map(client -> metaClient.getUserInfo(
            "Bearer " + client.getAccessToken().getTokenValue())).orElse(null);
        model.addAttribute("info", facebookUserInfo);
        return "main-layout";
    }
}
