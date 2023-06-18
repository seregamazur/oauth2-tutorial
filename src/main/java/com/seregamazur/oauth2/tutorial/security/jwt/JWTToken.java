package com.seregamazur.oauth2.tutorial.security.jwt;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class JWTToken {

    private String value;

    public JWTToken(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}