package com.seregamazur.oauth2.tutorial.client.model;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2AccessToken;
import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2RefreshToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2AccessTokenResponse {
    OAuth2AccessToken accessToken;
    OAuth2RefreshToken refreshToken;
}
