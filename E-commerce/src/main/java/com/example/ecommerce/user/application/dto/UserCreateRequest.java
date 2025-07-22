package com.example.ecommerce.user.application.dto;

import com.example.ecommerce.user.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @Schema(description = "이름", example = "홍길동")
    private String name;

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .build();
    }
}
