package com.example.ecommerce.docs.controller.point;

import com.example.ecommerce.docs.dto.CommApiResponse;
import com.example.ecommerce.point.application.dto.UserPointResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointMockController implements PointApi {

    @Override
    public CommApiResponse<UserPointResponse> getPoint(Long userId) {
        UserPointResponse response = new UserPointResponse(userId, 1000L);
        return CommApiResponse.success(response);
    }
}
