package com.seregamazur.oauth2.tutorial.client.model;

import java.io.Serializable;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2RefreshToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OAuth2AuthorizedClient implements Serializable {
    private OAuth2TokenSet accessToken;
    private OAuth2RefreshToken refreshToken;
}
