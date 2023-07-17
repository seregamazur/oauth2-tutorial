package com.seregamazur.oauth2.tutorial.crud;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

    private String id;
    private String login;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;
    private String password;
}
