package com.seregamazur.oauth2.tutorial.crud;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
    private boolean rememberMe;
}
