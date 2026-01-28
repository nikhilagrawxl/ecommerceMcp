package com.nikhil.ecommerce.service;

import com.nikhil.ecommerce.model.User;
import com.nikhil.ecommerce.dto.UserResponseDTO;
import com.nikhil.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(String name, User.UserType type) {
        User user = new User(name, type);
        User saved = userRepository.save(user);
        return new UserResponseDTO(saved.getUserId(), saved.getName(), saved.getType().name());
    }

    public UserResponseDTO getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserResponseDTO(user.getUserId(), user.getName(), user.getType().name());
    }
}
