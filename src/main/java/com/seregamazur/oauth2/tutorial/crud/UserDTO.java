package com.seregamazur.oauth2.tutorial.crud;

import java.io.Serializable;
import java.util.Date;

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
    private boolean hasGoogleAccount;
    private boolean hasGithubAccount;
    private boolean hasOktaAccount;
    private boolean hasFacebookAccount;
}
