package com.example.ecommerce.point.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPointRepository extends JpaRepository<PointJpaEntity, Long> {
}
