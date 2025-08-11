package com.example.ecommerce.coupon.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<CouponJpaEntity, Long> {
}
