package com.example.ecommerce.user.domain.repository;

import com.example.ecommerce.user.domain.model.User;

public interface UserRepository {
    User save(User user);
}
