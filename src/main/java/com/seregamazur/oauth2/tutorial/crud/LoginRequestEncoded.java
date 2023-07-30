package com.seregamazur.oauth2.tutorial.crud;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestEncoded {

    private String email;
    private String password;
    private boolean rememberMe;
}
