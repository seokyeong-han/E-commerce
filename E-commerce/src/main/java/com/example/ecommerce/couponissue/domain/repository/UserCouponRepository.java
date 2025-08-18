package com.example.ecommerce.couponissue.domain.repository;

import com.example.ecommerce.couponissue.domain.model.UserCoupon;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);
}
