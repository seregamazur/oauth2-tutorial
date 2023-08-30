package com.seregamazur.oauth2.tutorial.crud;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2TokenProvider;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

    private String id;
    private Date createdDate;
    private Date lastModifiedDate;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<OAuth2TokenProvider> authProviders;
    private boolean twoFactorEnabled;

}
