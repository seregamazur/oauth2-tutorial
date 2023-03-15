package com.seregamazur.oauth2.tutorial.client.model.okta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OktaUserInfo {
    private String sub;
    private String email;
    private String name;
}
