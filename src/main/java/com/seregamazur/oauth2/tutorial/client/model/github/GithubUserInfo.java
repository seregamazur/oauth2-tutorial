package com.seregamazur.oauth2.tutorial.client.model.github;

import java.util.List;

import lombok.Data;

@Data
public class GithubUserInfo {
    private String name;
    private String login;
    private List<String> scopes;
}
