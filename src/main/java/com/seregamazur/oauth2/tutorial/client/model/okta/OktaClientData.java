package com.seregamazur.oauth2.tutorial.client.model.okta;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "oktaClientData", url = "https://dev-12266902.okta.com")
public interface OktaClientData {

    @GetMapping(value = "/oauth2/default/v1/userinfo",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    OktaUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);

}
