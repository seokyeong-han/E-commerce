package com.example.ecommerce.user.application.dto;

import com.example.ecommerce.user.domain.model.User;

import java.time.LocalDateTime;

public class UserCreateRequest {
    private String name;

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .build();
    }
}
