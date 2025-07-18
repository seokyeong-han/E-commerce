package com.example.ecommerce.user.application.dto;

import com.example.ecommerce.user.domain.model.User;
import lombok.Setter;

@Setter
public class UserCreateResponse {
    private Long id;
    private String name;

    public UserCreateResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserCreateResponse from(User user) {
        return new UserCreateResponse(user.getId(), user.getName());
    }
}
