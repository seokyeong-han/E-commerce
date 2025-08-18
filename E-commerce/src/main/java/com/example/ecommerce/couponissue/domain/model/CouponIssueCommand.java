package com.example.ecommerce.couponissue.domain.model;

import lombok.Getter;

public class CouponIssueCommand {
    @Getter
    public static class Issue {
        private Long userId;
        private Long couponId;

        public Issue(Long userId, Long couponId) {
            this.userId = userId;
            this.couponId = couponId;
        }
    }
}
