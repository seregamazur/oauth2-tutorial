package com.seregamazur.oauth2.tutorial.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seregamazur.oauth2.tutorial.crud.LoginRequestEncoded;
import com.seregamazur.oauth2.tutorial.crud.ResponseUtil;
import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserDTO;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.service.UserService;
import com.seregamazur.oauth2.tutorial.security.jwt.JWTToken;
import com.seregamazur.oauth2.tutorial.security.jwt.UserAlreadyExistsException;
import com.seregamazur.oauth2.tutorial.service.token.JWTTokenProvider;
import com.seregamazur.oauth2.tutorial.utils.SecurityUtils;

@RestController
public class UserJWTController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider tokenCreationService;

    public UserJWTController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, JWTTokenProvider tokenCreationService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenCreationService = tokenCreationService;
    }

    @GetMapping(value = "/api/v1/identify")
    public ResponseEntity<UserDTO> identifyUser(@RequestParam @Valid @Email String email) {
        return ResponseUtil.wrapOrNotFound(userService.getUserByEmail(email));
    }

    @Transactional
    @PostMapping(value = "/api/v1/register")
    public ResponseEntity<JWTToken> registerUser(@RequestBody UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
        }
        User registeredUser = userService.createUser(userDTO);
        return ResponseEntity.ok(tokenCreationService.createJwt(registeredUser, false));
    }

    @Transactional
    @PostMapping(path = "/api/v1/authenticate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<JWTToken> authorize(LoginRequestEncoded loginRequestEncoded) {
        User userByEmail = userRepository.findByEmail(loginRequestEncoded.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("No such user with email:" + loginRequestEncoded.getEmail()));

        if (!passwordEncoder.matches(loginRequestEncoded.getPassword(), userByEmail.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return ResponseEntity.ok(tokenCreationService.createJwt(userByEmail, loginRequestEncoded.isRememberMe()));
    }
}
