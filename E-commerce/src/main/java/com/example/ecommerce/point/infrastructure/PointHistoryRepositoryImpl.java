package com.example.ecommerce.point.infrastructure;

import com.example.ecommerce.point.domain.model.PointHistory;
import com.example.ecommerce.point.domain.repository.PointHistoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {
    private final JpaPointHistoryRepository jpaRepository;

    public PointHistoryRepositoryImpl(JpaPointHistoryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PointHistory save(PointHistory pointHistory) {
        PointHistoryJpaEntity saved = jpaRepository.save(PointHistoryJpaEntity.fromDomain(pointHistory));
        return saved.toDomain();
    }
}
