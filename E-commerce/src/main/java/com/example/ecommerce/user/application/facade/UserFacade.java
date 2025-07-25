package com.example.ecommerce.user.application.facade;

import com.example.ecommerce.user.domain.model.User;
import com.example.ecommerce.user.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ecommerce.user.application.dto.UserCreateRequest;
import com.example.ecommerce.user.application.dto.UserCreateResponse;

@Component
public class UserFacade {

    @Autowired
    UserService userService;

    public UserCreateResponse createUserWithInitialPoint(UserCreateRequest request) {
        User user = userService.create(request); // 1. 유저 생성
        // 유저 생성시 포인트 정보 생성
        // pointService.recordJoinBonus(user, 0L); // 2. 초기 포인트 기록
        return UserCreateResponse.from(user);
    }
}
