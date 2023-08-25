package com.seregamazur.oauth2.tutorial.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.seregamazur.oauth2.tutorial.security.jwt.TokenVerificationService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenVerificationService tokenVerificationService;

    public SecurityConfig(TokenVerificationService tokenVerificationService) {
        this.tokenVerificationService = tokenVerificationService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .cors()
            .and()
            .headers()
            .and()
            .authorizeRequests()
            .antMatchers("/oauth2/authorization/google").permitAll()
            .antMatchers("/oauth2/authorization/google/callback").permitAll()
            .and()
            .apply(securityConfigurerAdapter());
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenVerificationService);
    }
}