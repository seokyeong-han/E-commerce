package com.example.ecommerce.docs.controller.point;

import com.example.ecommerce.docs.dto.CommApiResponse;
import com.example.ecommerce.point.application.dto.PointChargeRequest;
import com.example.ecommerce.point.application.dto.PointChargeResponse;
import com.example.ecommerce.point.application.dto.UserPointResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointMockController implements PointApi {
    //Swagger 예시 응답 제공 (Mock 데이터)
    @Override
    public CommApiResponse<UserPointResponse> getPoint(Long userId) {
        UserPointResponse response = new UserPointResponse(userId, 1000L);
        return CommApiResponse.success(response);
    }

    @Override
    public CommApiResponse<PointChargeResponse> charge(PointChargeRequest request) {
        PointChargeResponse response = new PointChargeResponse(1L, 1100L, 100L);
        return CommApiResponse.success(response);
    }
}
