package com.example.ecommerce.docs.controller;

import com.example.ecommerce.docs.dto.CommApiResponse;
import com.example.ecommerce.user.application.dto.UserCreateRequest;
import com.example.ecommerce.user.application.dto.UserCreateResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMockController implements UserApi {

    @Override
    public CommApiResponse<UserCreateResponse> createUser(UserCreateRequest request) {
        // 가짜 응답 생성
        UserCreateResponse response = new UserCreateResponse(1L, request.getName());

        // CommApiResponse로 감싸서 리턴
        return CommApiResponse.success(response);
    }
}
