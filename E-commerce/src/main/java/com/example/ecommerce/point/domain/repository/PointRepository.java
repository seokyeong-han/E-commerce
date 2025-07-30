package com.example.ecommerce.point.domain.repository;

import com.example.ecommerce.point.domain.model.Point;

public interface PointRepository {
    Point save(Point point);
}
