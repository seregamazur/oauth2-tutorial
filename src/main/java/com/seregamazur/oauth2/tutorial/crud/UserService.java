package com.seregamazur.oauth2.tutorial.crud;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seregamazur.oauth2.tutorial.mapper.UserMapper;

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

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDto(users);
    }

    public Optional<UserDTO> getUserById(String id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user = userRepository.save(user);
        return userMapper.toDto(user);
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
