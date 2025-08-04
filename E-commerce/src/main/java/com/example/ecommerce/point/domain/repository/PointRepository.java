package com.example.ecommerce.point.domain.repository;

import com.example.ecommerce.point.domain.model.Point;

import java.util.Optional;

public interface PointRepository {
    Point save(Point point);
    Optional<Point> findByUserId(Long userId);
}
