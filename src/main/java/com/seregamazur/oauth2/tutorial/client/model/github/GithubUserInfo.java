package com.seregamazur.oauth2.tutorial.client.model.github;

import lombok.Data;

@Data
public class GithubUserInfo {
    private String id;
    private String name;
    private String email;
}
