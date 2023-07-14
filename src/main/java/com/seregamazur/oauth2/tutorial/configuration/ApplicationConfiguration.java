package com.seregamazur.oauth2.tutorial.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.JwtVerifiers;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {

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
    public AccessTokenVerifier oktaAccessTokenVerifier() {
        return JwtVerifiers.accessTokenVerifierBuilder()
            .setIssuer(issuer)
            .setAudience(audience)
            .build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000") // Replace with the origin of your React application
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

}
