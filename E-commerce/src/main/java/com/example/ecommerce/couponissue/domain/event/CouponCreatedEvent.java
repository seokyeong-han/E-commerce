package com.example.ecommerce.couponissue.domain.event;

public class CouponCreatedEvent {
    private final Long couponId;

    public CouponCreatedEvent(Long couponId) {
        this.couponId = couponId;
    }

    public Long getCouponId() {
        return couponId;
    }
}
