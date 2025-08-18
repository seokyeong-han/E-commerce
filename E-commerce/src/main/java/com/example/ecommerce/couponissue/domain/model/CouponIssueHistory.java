package com.example.ecommerce.couponissue.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CouponIssueHistory {
    private Long id;
    private Long userId;
    private Long couponId;
    private String action; // "ISSUED"
    private LocalDateTime createdAt;

    public static CouponIssueHistory issued(Long userId, Long couponId) {
        return CouponIssueHistory.builder()
                .userId(userId)
                .couponId(couponId)
                .action("ISSUED")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
