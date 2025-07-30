package com.example.ecommerce.point.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointHistoryRepository extends JpaRepository<PointHistoryJpaEntity, Long> {
}
