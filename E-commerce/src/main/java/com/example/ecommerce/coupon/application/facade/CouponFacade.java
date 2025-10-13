package com.example.ecommerce.coupon.application.facade;

import com.example.ecommerce.coupon.application.dto.CreateCouponRequest;
import com.example.ecommerce.coupon.application.dto.CreateCouponResponse;
import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.model.CouponCommand;
import com.example.ecommerce.coupon.domain.service.CouponRedisService;
import com.example.ecommerce.coupon.domain.service.CouponService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CouponFacade {
    private final CouponService couponService;
    private final CouponRedisService couponRedisService;

    public CouponFacade(CouponService couponService
            ,CouponRedisService couponRedisService) {
        this.couponService = couponService;
        this.couponRedisService = couponRedisService;
    }

    @Transactional
    public CreateCouponResponse createCoupon(CreateCouponRequest req){
        //dto -> command
        CouponCommand.Create command = new CouponCommand.Create(
                req.getName(),
                req.getType(),
                req.getDiscountAmount(),
                req.getDiscountRate(),
                req.getTotalQuantity(),
                req.getActiveFrom(),
                req.getExpiredAt());
        //쿠폰생성
        Coupon coupon = couponService.createCoupon(command);

        return CreateCouponResponse.from(coupon);
    }

    //쿠폰 발급 대기열 등록
    public void issueCouponRequest(Long couponId, Long userId) {
        couponRedisService.addCouponIssueRequestToQueue(couponId, userId);
    }

    //Scheduler 쿠폰 발급 프로세스
    public void processCouponIssuance(Long couponId, Long userId) {
        couponService.issueCouponToUser(couponId, userId);
    }
}
