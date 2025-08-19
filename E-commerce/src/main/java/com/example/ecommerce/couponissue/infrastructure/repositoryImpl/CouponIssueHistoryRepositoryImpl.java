package com.example.ecommerce.couponissue.infrastructure.repositoryImpl;

import com.example.ecommerce.couponissue.domain.model.CouponIssueHistory;
import com.example.ecommerce.couponissue.domain.repository.CouponIssueHistoryRepository;
import com.example.ecommerce.couponissue.infrastructure.entity.CouponIssueHistoryJpaEntity;
import com.example.ecommerce.couponissue.infrastructure.jpaRepository.JpaCouponIssueHistoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CouponIssueHistoryRepositoryImpl implements CouponIssueHistoryRepository {
    private final JpaCouponIssueHistoryRepository jpaRepository;

    public  CouponIssueHistoryRepositoryImpl(JpaCouponIssueHistoryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public CouponIssueHistory save(CouponIssueHistory couponIssueHistory) {
        CouponIssueHistoryJpaEntity saved = jpaRepository.save(CouponIssueHistoryJpaEntity.fromDomain(couponIssueHistory));
        return saved.toDomain();
    }
}
