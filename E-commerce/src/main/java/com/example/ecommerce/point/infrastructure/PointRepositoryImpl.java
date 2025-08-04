package com.example.ecommerce.point.infrastructure;

import com.example.ecommerce.point.domain.model.Point;
import com.example.ecommerce.point.domain.repository.PointRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointRepositoryImpl implements PointRepository {
    private final JpaPointRepository jpaRepository;

    public PointRepositoryImpl(JpaPointRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Point save(Point point){
        PointJpaEntity saved = jpaRepository.save(PointJpaEntity.fromDomain(point));
        return saved.toDomain();
    }

    @Override
    public Optional<Point> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).map(PointJpaEntity::toDomain);
    }
}
