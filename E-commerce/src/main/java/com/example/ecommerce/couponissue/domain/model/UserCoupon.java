package com.example.ecommerce.couponissue.domain.model;

import com.example.ecommerce.coupon.domain.model.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserCoupon {
    private Long id;
    private Long userId;
    private Long couponId;
    private boolean used;  //쿠폰 사용여부
    private LocalDateTime issuedAt;

    public static UserCoupon issue(Long userId, Long couponId) {
        return UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .used(false)
                .build();
    }
}
