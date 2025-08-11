package com.example.ecommerce.coupon.domain.repository;

import com.example.ecommerce.coupon.domain.model.Coupon;

public interface CouponRepository {
    Coupon save(Coupon coupon);
}
