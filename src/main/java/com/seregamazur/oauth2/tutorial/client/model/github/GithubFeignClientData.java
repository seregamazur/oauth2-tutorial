package com.seregamazur.oauth2.tutorial.client.model.github;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.seregamazur.oauth2.tutorial.client.model.IdToken;

@FeignClient(name = "githubClientData", url = "https://api.github.com")
public interface GithubFeignClientData {

    @GetMapping(value = "/user?fields=id,name,email",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    IdToken verifyToken(@RequestHeader("Authorization") String accessToken);
}
