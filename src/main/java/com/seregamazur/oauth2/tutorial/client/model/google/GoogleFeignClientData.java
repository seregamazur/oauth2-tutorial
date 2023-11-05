package com.seregamazur.oauth2.tutorial.client.model.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleClientData", url = "https://www.googleapis.com")
public interface GoogleFeignClientData {

    @GetMapping(value = "oauth2/v1/userinfo?alt=json",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    GoogleUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);

}
