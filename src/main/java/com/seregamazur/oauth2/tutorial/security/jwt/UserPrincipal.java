package com.seregamazur.oauth2.tutorial.security.jwt;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.seregamazur.oauth2.tutorial.client.model.LoginType;

public class UserPrincipal extends User {
    private String id;
    private String fullName;
    private String email;
    private LoginType loginType;

    public UserPrincipal(String id, String email, String password, String fullName, String loginType) {
        super(email, password, Collections.EMPTY_LIST);
        this.email = email;
        this.fullName = fullName;
        this.loginType = LoginType.valueOf(loginType);
    }

    public UserPrincipal(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
