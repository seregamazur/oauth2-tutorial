package com.seregamazur.oauth2.tutorial.client.model.token;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2TokenProvider;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OAuth2TokenSet implements Serializable {

    public OAuth2TokenSet(String tokenType, String accessToken, String scope) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.scope = scope;
    }

    public OAuth2TokenSet(String tokenType, String accessToken, Instant issuedAt, Instant expiresAt, String scope) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.scope = scope;
    }

    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("id_token")
    private String idToken;
    @JsonProperty("issued_at")
    private Instant issuedAt;
    @JsonProperty("expires_at")
    private Instant expiresAt;
    @JsonProperty("scope")
    private String scope;
    private OAuth2TokenProvider OAuth2TokenProvider;

}
