package com.seregamazur.oauth2.tutorial.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.crud.User;
import com.seregamazur.oauth2.tutorial.crud.UserDTO;
import com.seregamazur.oauth2.tutorial.crud.UserRepository;
import com.seregamazur.oauth2.tutorial.mapper.UserMapper;
import com.seregamazur.oauth2.tutorial.utils.SecurityUtils;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDTO> getUser() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findByEmail)
            .map(userMapper::toDto);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDto(users);
    }

    public Optional<UserDTO> enableTwoFactorCode(String key) {
        Optional<User> byEmail = userRepository
            .findByEmail(SecurityUtils.getCurrentUserLogin().get());
        byEmail.get().setTwoFactorEnabled(true);
        byEmail.get().setTwoFactorSecret(key);
        return byEmail
            .map(userRepository::save)
            .map(userMapper::toDto);
    }

    public Optional<UserDTO> getUserById(String id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }

    public User createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user = userRepository.save(user);
        return user;
    }

    public String getTwoFactorSecret() {
        Optional<User> byEmail = userRepository
            .findByEmail(SecurityUtils.getCurrentUserLogin().get());
        return byEmail.get().getTwoFactorSecret();
    }

    public UserDTO updateUser(String id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(existingUser.get().getId());
            User updatedUser = userRepository.save(user);
            return userMapper.toDto(updatedUser);
        } else {
            return null;
        }
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
