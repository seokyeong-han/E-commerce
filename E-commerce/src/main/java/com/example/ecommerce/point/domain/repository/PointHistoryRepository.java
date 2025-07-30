package com.example.ecommerce.point.domain.repository;

import com.example.ecommerce.point.domain.model.PointHistory;

public interface PointHistoryRepository {
    PointHistory save(PointHistory pointHistory);
}
