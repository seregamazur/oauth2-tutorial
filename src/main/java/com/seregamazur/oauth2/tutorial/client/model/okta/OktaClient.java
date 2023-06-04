package com.seregamazur.oauth2.tutorial.client.model.okta;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;

@FeignClient(name = "oktaClient", url = "https://dev-12266902.okta.com")
public interface OktaClient {

    @PostMapping(value = "/oauth2/default/v1/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    OAuth2AccessToken getAccessToken(@RequestBody Map<String, ?> formData);

}