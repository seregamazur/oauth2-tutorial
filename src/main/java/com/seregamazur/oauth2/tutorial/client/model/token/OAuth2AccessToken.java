package com.seregamazur.oauth2.tutorial.client.model.token;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OAuth2AccessToken implements Serializable {

    public OAuth2AccessToken(String tokenType, String tokenValue, String scope) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
        this.scope = scope;
    }

    public OAuth2AccessToken(String tokenType, String tokenValue, Instant issuedAt, Instant expiresAt, String scope) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.scope = scope;
    }

    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String tokenValue;
    @JsonProperty("issued_at")
    private Instant issuedAt;
    @JsonProperty("expires_at")
    private Instant expiresAt;
    @JsonProperty("scope")
    private String scope;

}
