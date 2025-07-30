package com.example.ecommerce.point.application.facade;

import com.example.ecommerce.point.domain.service.PointService;
import org.springframework.stereotype.Component;

@Component
public class PointFacade {
    private final PointService pointService;

    public PointFacade(PointService pointService) {
        this.pointService = pointService;
    }
}
