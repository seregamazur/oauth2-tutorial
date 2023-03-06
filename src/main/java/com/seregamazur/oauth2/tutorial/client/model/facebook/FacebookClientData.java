package com.seregamazur.oauth2.tutorial.client.model.facebook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "facebookClientData", url = "https://graph.facebook.com")
public interface FacebookClientData {

    @GetMapping(value = "me?fields=id,email,last_name,first_name",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    FacebookUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);

}
