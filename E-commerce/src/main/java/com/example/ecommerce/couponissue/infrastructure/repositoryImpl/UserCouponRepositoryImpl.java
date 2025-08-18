package com.example.ecommerce.couponissue.infrastructure.repositoryImpl;

import com.example.ecommerce.couponissue.domain.model.UserCoupon;
import com.example.ecommerce.couponissue.domain.repository.UserCouponRepository;
import com.example.ecommerce.couponissue.infrastructure.entity.UserCouponJpaEntity;
import com.example.ecommerce.couponissue.infrastructure.jpaRepository.JpaUserCouponRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {
    private final JpaUserCouponRepository jpaRepository;

    public UserCouponRepositoryImpl(JpaUserCouponRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        UserCouponJpaEntity saved = jpaRepository.save(UserCouponJpaEntity.fromDomain(userCoupon));
        return saved.toDomain();
    }
}
