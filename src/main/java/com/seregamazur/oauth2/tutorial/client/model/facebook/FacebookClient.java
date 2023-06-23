package com.seregamazur.oauth2.tutorial.client.model.facebook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

@FeignClient(name = "facebookClient", url = "https://graph.facebook.com")
public interface FacebookClient {

    @GetMapping(value = "v16.0/oauth/access_token?client_id={clientId}&scope=email%20openid&client_secret={clientSecret}&redirect_uri={redirectUri}&code={authCode}",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    OAuth2AccessToken getAccessToken(@PathVariable("clientId") String clientId,
        @PathVariable("clientSecret") String clientSecret,
        @PathVariable("redirectUri") String redirectUri,
        @PathVariable("authCode") String authCode);

}