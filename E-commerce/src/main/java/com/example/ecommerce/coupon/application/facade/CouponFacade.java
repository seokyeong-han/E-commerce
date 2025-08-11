package com.example.ecommerce.coupon.application.facade;

import com.example.ecommerce.coupon.application.dto.CreateCouponRequest;
import com.example.ecommerce.coupon.application.dto.CreateCouponResponse;
import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.model.CouponCommand;
import com.example.ecommerce.coupon.domain.service.CouponService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CouponFacade {
    private final CouponService couponService;

    public CouponFacade(CouponService couponService){
        this.couponService = couponService;
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
                req.getExpiredAt());
        //쿠폰생성
        Coupon coupon = couponService.createCoupon(command);
        //히스토리 저장
        return CreateCouponResponse.from(coupon);
    }

}
