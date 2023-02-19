package com.seregamazur.oauth2.tutorial.client.model;

public enum OAuth2GrantType {
    AUTHORIZATION_CODE("authorization_code");

    private String value;

    OAuth2GrantType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
