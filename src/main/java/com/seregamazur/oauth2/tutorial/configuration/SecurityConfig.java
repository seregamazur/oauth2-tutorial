package com.seregamazur.oauth2.tutorial.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.seregamazur.oauth2.tutorial.security.jwt.JWTConfigurer;
import com.seregamazur.oauth2.tutorial.security.jwt.TokenProvider;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    public SecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
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
        return new JWTConfigurer(tokenProvider);
    }
}