package com.example.ecommerce.docs.controller;

import com.example.ecommerce.user.application.dto.UserCreateRequest;
import com.example.ecommerce.user.application.dto.UserCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "유저 API", description = "유저 관련 API")
@RequestMapping("/api/users")
public interface UserControllerDocs {

    @Operation(summary = "유저 생성", description = "유저 정보를 받아 새로운 유저를 생성합니다.")
    @PostMapping("/create")
    ResponseEntity<UserCreateResponse> create(
            @RequestBody
            @Parameter(description = "생성할 유저 정보", required = true)
            UserCreateRequest request
    );
}
