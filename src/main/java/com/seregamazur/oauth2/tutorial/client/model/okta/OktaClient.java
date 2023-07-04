package com.seregamazur.oauth2.tutorial.client.model.okta;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

@FeignClient(name = "oktaClient", url = "https://dev-12266902.okta.com")
public interface OktaClient {

    @PostMapping(value = "/oauth2/default/v1/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    OAuth2TokenSet getAccessToken(@RequestBody Map<String, ?> formData);

    @GetMapping(value = "/oauth2/default/v1/userinfo",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    OktaUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);

}