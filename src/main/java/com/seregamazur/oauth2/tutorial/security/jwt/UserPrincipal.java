package com.seregamazur.oauth2.tutorial.security.jwt;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {
    private String id;
    private String fullName;

    public UserPrincipal(String id, String username, String password, String fullName) {
        super(username, password, Collections.EMPTY_LIST);
        this.id = id;
        this.fullName = fullName;
    }

    public UserPrincipal(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
