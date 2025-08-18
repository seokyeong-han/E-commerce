package com.example.ecommerce.coupon.domain.repository;

import com.example.ecommerce.coupon.domain.model.Coupon;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findById(Long id);
    Coupon save(Coupon coupon);
}
