package com.seregamazur.oauth2.tutorial.client.model.github;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2AccessTokenRequest;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

@FeignClient(name = "githubClient", url = "https://github.com")
public interface GithubClientAuthorization {

    @PostMapping(value = "/login/oauth/access_token/",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    OAuth2TokenSet getAccessToken(OAuth2AccessTokenRequest OAuth2AccessTokenRequest);

}
