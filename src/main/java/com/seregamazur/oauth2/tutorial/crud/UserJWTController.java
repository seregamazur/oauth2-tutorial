package com.seregamazur.oauth2.tutorial.crud;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.service.JWTTokenCreationService;

@RestController
public class UserJWTController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenCreationService tokenCreationService;

    public UserJWTController(UserService userService, PasswordEncoder passwordEncoder, JWTTokenCreationService tokenCreationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenCreationService = tokenCreationService;
    }

    @GetMapping(value = "/api/v1/identify")
    public ResponseEntity<Boolean> identifyUser(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    @Transactional
    @PostMapping(value = "/api/v1/register")
    public ResponseEntity<UserDTO> registerUser(@RequestParam UserDTO userDTO) throws URISyntaxException {
        UserDTO registeredUser = userService.createUser(userDTO);
        return ResponseEntity.created(new URI("/api/v1/register/" + registeredUser.getId()))
            .body(registeredUser);
    }

    @Transactional
    @PostMapping(path = "/api/v1/authenticate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<JWTToken> authorize(LoginRequestEncoded loginRequestEncoded) {
        User userByEmail = userService.getUserByEmail(loginRequestEncoded.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("No such user with email:" + loginRequestEncoded.getEmail()));

        if (!passwordEncoder.matches(loginRequestEncoded.getPassword(), userByEmail.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return ResponseEntity.ok(tokenCreationService.createJwt(userByEmail, loginRequestEncoded.isRememberMe()));
    }
}
