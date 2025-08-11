package com.example.ecommerce.coupon.domain.service;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.model.CouponCommand;
import com.example.ecommerce.coupon.domain.model.CouponHistory;
import com.example.ecommerce.coupon.domain.repository.CouponHistoryRepository;
import com.example.ecommerce.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    public CouponService(CouponRepository couponRepository, CouponHistoryRepository couponHistoryRepository) {
        this.couponRepository = couponRepository;
        this.couponHistoryRepository = couponHistoryRepository;
    }

    public Coupon createCoupon(CouponCommand.Create command) {
        //쿠폰생성
        Coupon coupon = Coupon.create(command);
        Coupon saved = couponRepository.save(coupon);
        //쿠폰히스토리 등록
        couponHistoryRepository.save(CouponHistory.created(saved));
        return saved;
    }
}
