package com.example.ecommerce.couponissue.infrastructure.jpaRepository;

import com.example.ecommerce.couponissue.infrastructure.entity.UserCouponJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserCouponRepository extends JpaRepository<UserCouponJpaEntity, Long> {
}
