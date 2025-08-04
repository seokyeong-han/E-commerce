package com.example.ecommerce.point.application.facade;

import com.example.ecommerce.point.application.dto.UserPointResponse;
import com.example.ecommerce.point.domain.model.Point;
import com.example.ecommerce.point.domain.service.PointService;
import com.example.ecommerce.user.domain.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PointFacade {
    private final PointService pointService;
    private final UserService userService;

    public PointFacade(PointService pointService,  UserService userService) {
        this.pointService = pointService;
        this.userService = userService;
    }

    public UserPointResponse getUserPoints(Long userId){
        //유저검증
        userService.findById(userId);
        //포인트 조회
        Point point = pointService.getUserPints(userId);
        return UserPointResponse.from(point);
    }

}
