package com.example.ecommerce.couponissue.infrastructure.jpaRepository;

import com.example.ecommerce.couponissue.infrastructure.entity.CouponIssueHistoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponIssueHistoryRepository
        extends JpaRepository<CouponIssueHistoryJpaEntity, Long> {
}
