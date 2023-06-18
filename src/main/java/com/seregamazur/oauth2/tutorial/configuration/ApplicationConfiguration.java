package com.seregamazur.oauth2.tutorial.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
            new com.google.api.client.http.javanet.NetHttpTransport(),
            new com.google.api.client.json.gson.GsonFactory())
            .build();
    }

}
