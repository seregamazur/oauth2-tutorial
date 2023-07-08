package com.seregamazur.oauth2.tutorial.client.model.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2AccessTokenRequest;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

@FeignClient(name = "googleClient", url = "https://oauth2.googleapis.com")
public interface GoogleClient {

    @PostMapping(value = "/token",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    OAuth2TokenSet getAccessToken(OAuth2AccessTokenRequest OAuth2AccessTokenRequest);

}