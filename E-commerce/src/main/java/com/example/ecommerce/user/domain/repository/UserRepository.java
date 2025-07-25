package com.example.ecommerce.user.domain.repository;

import com.example.ecommerce.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
}
