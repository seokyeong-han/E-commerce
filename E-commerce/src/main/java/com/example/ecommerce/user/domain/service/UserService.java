package com.example.ecommerce.user.domain.service;

import com.example.ecommerce.user.application.dto.UserCreateRequest;
import com.example.ecommerce.user.domain.model.User;
import com.example.ecommerce.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository; // 주입 필요

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(UserCreateRequest request) {
        return userRepository.save(request.toEntity());
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
