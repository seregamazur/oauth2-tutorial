package com.seregamazur.oauth2.tutorial.verifier.oauth2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2TokenPair {
    private String openIdToken;
    private String accessToken;
}
