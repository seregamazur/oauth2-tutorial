package com.seregamazur.oauth2.tutorial.security.jwt;

import java.time.Instant;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JWT {
    String tokenValue;

    Instant issuedAt;

    Instant expiresAt;

    Map<String, Object> claims;
}
