package com.example.ecommerce.coupon.domain.repository;

import com.example.ecommerce.coupon.domain.model.CouponHistory;

public interface CouponHistoryRepository {
    CouponHistory save(CouponHistory couponHistory);
}
