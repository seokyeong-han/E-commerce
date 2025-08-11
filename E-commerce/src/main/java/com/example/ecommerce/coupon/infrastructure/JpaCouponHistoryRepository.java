package com.example.ecommerce.coupon.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponHistoryRepository extends JpaRepository<CouponHistoryJpaEntity, Long> {
}
