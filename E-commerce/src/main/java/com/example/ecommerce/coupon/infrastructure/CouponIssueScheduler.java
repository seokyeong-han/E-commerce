package com.example.ecommerce.coupon.infrastructure;

import com.example.ecommerce.coupon.application.facade.CouponFacade;
import com.example.ecommerce.coupon.domain.service.CouponRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueScheduler {
    private final CouponRedisService couponRedisService;
    private final CouponFacade  couponFacade;


    @Scheduled(fixedDelay = 500) //0.5초 마다 실행
    public void processQueue() {
        //대기열에있는 쿠폰발급 리스트 가져오기
        List<String> requests = couponRedisService.popRequestsFromQueue(100L);
        //큐가 비어있으면 아무것도 하지 않고 종료
        if (requests == null || requests.isEmpty()) {
            return;
        }
        log.info("Processing {} coupon issue requests from queue.", requests.size());
        //대기열 데이터 순회 처리
        for (String request : requests) {
            try {
                String[] parts = request.split(":");
                Long couponId = Long.parseLong(parts[0]);
                Long userId = Long.parseLong(parts[1]);

                //실제 발급 처리
                couponFacade.processCouponIssuance(couponId, userId);
            }catch (Exception e){
                //개별 요청 처리 중 실패 시 처리
                //여기서 실패 해도 for문은 멈추지 않고 계속 다음 요청 처리
                log.error("Failed to process coupon issue request: {}. Error: {}", request, e.getMessage());
            }
        }
    }
}
