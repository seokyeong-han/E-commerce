package com.example.ecommerce.coupon.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;

public class CouponCommand {
    @Getter
    public static class Create {
        private final String name;
        private final DiscountType type;
        private final Long discountAmount;
        private final Integer discountRate;
        private final Integer totalQuantity;
        private final LocalDateTime expiredAt;

        public Create(String name, DiscountType type, Long discountAmount, Integer discountRate, Integer totalQuantity, LocalDateTime expiredAt) {
            this.name = name;
            this.type = type;
            this.discountAmount = discountAmount;
            this.discountRate = discountRate;
            this.totalQuantity = totalQuantity;
            this.expiredAt = expiredAt;
        }

    }
}
