package com.seregamazur.oauth2.tutorial.server;

import java.time.Instant;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OAuthClientDetails {
    @Id
    private String clientId;
    private String clientSecret;
    private Set<String> authorizedGrantTypes;
    private Set<String> scopes;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private String redirectUri;
    // getters and setters
}

@Document
class OAuthAccessToken {
    @Id
    private String tokenId;
    private String clientId;
    private String username;
    private String authenticationId;
    private Set<String> scopes;
    private Instant expiration;
    // getters and setters
}