package com.example.ecommerce.couponissue.infrastructure.entity;

import com.example.ecommerce.couponissue.domain.model.CouponIssueHistory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_issue_history")
public class CouponIssueHistoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "action", nullable = false, length = 20)
    private String action; // 예: "ISSUED"

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public CouponIssueHistory toDomain() {
        return CouponIssueHistory.builder()
                .id(this.id)
                .userId(this.userId)
                .couponId(this.couponId)
                .action(this.action)
                .createdAt(this.createdAt)
                .build();
    }

    public static CouponIssueHistoryJpaEntity fromDomain(CouponIssueHistory domain) {
        return CouponIssueHistoryJpaEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .couponId(domain.getCouponId())
                .action(domain.getAction())
                .createdAt(domain.getCreatedAt())
                .build();
    }

}
