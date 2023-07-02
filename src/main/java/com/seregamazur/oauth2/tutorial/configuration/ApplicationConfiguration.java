package com.seregamazur.oauth2.tutorial.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.JwtVerifiers;

@Configuration
public class ApplicationConfiguration {

    @Value("${okta.issuer}")
    private String issuer;

    @Value("${okta.audience}")
    private String audience;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
            new com.google.api.client.http.javanet.NetHttpTransport(),
            new com.google.api.client.json.gson.GsonFactory())
            .build();
    }

    @Bean
    public AccessTokenVerifier oktaTokenVerifier() {
        return JwtVerifiers.accessTokenVerifierBuilder()
            .setIssuer(issuer)
            .setAudience(audience)
            .build();
    }

}
