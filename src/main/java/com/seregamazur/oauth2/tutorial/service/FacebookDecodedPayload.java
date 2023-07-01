package com.seregamazur.oauth2.tutorial.service;

import java.util.List;

import lombok.Data;

@Data
public class FacebookDecodedPayload {
    private String issuer;
    private List<String> audience;
    private String subject;
    private long issuedAt;
    private long expiration;
    private String jwtId;
    private String nonce;
    private String accessTokenHash;
    private String email;
    private String givenName;
    private String familyName;
    private String name;
    private String picture;


}
