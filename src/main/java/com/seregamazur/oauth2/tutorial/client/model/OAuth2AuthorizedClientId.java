package com.seregamazur.oauth2.tutorial.client.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OAuth2AuthorizedClientId implements Serializable {
    private OAuth2TokenProvider clientRegistrationId;
    private String principalName;
}
