package com.seregamazur.oauth2.tutorial.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTToken {

    private String accessToken;

    public JWTToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("access_token")
    String getAccessToken() {
        return accessToken;
    }
}