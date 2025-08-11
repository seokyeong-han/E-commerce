package com.example.ecommerce.coupon.infrastructure;

import com.example.ecommerce.coupon.domain.model.CouponHistory;
import com.example.ecommerce.coupon.domain.repository.CouponHistoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CouponHistoryRepositoryImpl implements CouponHistoryRepository {
    private final JpaCouponHistoryRepository jpaRepository;

    public CouponHistoryRepositoryImpl(JpaCouponHistoryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public CouponHistory save(CouponHistory couponHistory) {
        CouponHistoryJpaEntity saved = jpaRepository.save(CouponHistoryJpaEntity.fromDomain(couponHistory));
        return saved.toDomain();
    }
}
