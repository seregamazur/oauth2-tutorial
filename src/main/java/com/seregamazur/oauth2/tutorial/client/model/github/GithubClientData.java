package com.seregamazur.oauth2.tutorial.client.model.github;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "githubClientData", url = "https://api.github.com")
public interface GithubClientData {

    @GetMapping(value = "/user",
        headers = { "Content-Type=application/json", "Accept=application/json" })
    GithubUserInfo getUserInfo(@RequestHeader("Authorization") String accessToken);
}
