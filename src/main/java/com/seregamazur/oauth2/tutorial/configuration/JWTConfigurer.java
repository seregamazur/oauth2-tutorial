package com.seregamazur.oauth2.tutorial.configuration;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.seregamazur.oauth2.tutorial.security.LoggingFilter;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTFilter;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenVerificationService;

public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenVerificationService tokenVerificationService;

    public JWTConfigurer(TokenVerificationService tokenVerificationService) {
        this.tokenVerificationService = tokenVerificationService;
    }

    @Override
    public void configure(HttpSecurity http) {
        JWTFilter customFilter = new JWTFilter(tokenVerificationService);
        LoggingFilter loggingFilter = new LoggingFilter();
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(loggingFilter, JWTFilter.class);
    }
}
