package com.seregamazur.oauth2.tutorial.client.model.facebook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.seregamazur.oauth2.tutorial.service.IdToken;

@FeignClient(name = "facebookClientData", url = "https://graph.facebook.com")
public interface FacebookClientData {

    @GetMapping(value = "me?fields=id,email,last_name,first_name",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    FacebookUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);

    @GetMapping(value = "v14.0/me?fields=id,name,email&access_token={accessToken}",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    IdToken verifyToken(@PathVariable("accessToken") String accessToken);

}
