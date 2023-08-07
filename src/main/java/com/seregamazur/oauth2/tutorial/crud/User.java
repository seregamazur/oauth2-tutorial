package com.seregamazur.oauth2.tutorial.crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.seregamazur.oauth2.tutorial.client.model.LoginProvider;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "users")
@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {
    @Id
    private String id;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private List<LoginProvider> authProviders = new ArrayList<>();

    public User(String email, List<LoginProvider> authProviders) {
        this.email = email;
        this.authProviders = authProviders;
    }

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(Date createdDate, Date lastModifiedDate, String firstName, String lastName, String email, String password) {
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public boolean hasProvider(LoginProvider provider) {
        return authProviders.contains(provider);
    }
}
