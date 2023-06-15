package com.seregamazur.oauth2.tutorial.crud;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String login;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;
    private String password;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
