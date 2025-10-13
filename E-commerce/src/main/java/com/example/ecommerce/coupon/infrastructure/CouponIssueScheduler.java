package com.example.ecommerce.coupon.infrastructure;

import com.example.ecommerce.coupon.application.facade.CouponFacade;
import com.example.ecommerce.coupon.domain.service.CouponRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponIssueScheduler {
    private final CouponRedisService couponRedisService;
    private final CouponFacade  couponFacade;


    @Scheduled(fixedDelay = 500) //0.5초 마다 실행
    public void processQueue() {
        //대기열에있는 쿠폰발급 리스트 가져오기
        List<String> requests = couponRedisService.popRequestsFromQueue(100L);
    }
}
