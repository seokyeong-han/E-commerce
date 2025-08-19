package com.example.ecommerce.couponissue.domain.repository;

import com.example.ecommerce.couponissue.domain.model.CouponIssueHistory;

public interface CouponIssueHistoryRepository {
    CouponIssueHistory save(CouponIssueHistory couponIssueHistory);
}
