package com.example.ecommerce.coupon.infrastructure;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.repository.CouponRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepositoryImpl implements CouponRepository {
    private final JpaCouponRepository jpaRepository;

    public CouponRepositoryImpl(JpaCouponRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponJpaEntity saved = jpaRepository.save(CouponJpaEntity.fromDomain(coupon));
        return saved.toDomain();
    }
}
