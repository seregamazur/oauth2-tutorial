package com.seregamazur.oauth2.tutorial.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2AccessTokenRequest {

    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    private String code;
    @JsonProperty("redirect_uri")
    private String redirectUri;
}
