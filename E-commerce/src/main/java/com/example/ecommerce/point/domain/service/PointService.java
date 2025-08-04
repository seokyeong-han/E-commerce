package com.example.ecommerce.point.domain.service;

import com.example.ecommerce.point.domain.model.Point;
import com.example.ecommerce.point.domain.model.PointHistory;
import com.example.ecommerce.point.domain.model.PointType;
import com.example.ecommerce.point.domain.repository.PointHistoryRepository;
import com.example.ecommerce.point.domain.repository.PointRepository;
import com.example.ecommerce.user.domain.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PointService {
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PointService(PointRepository pointRepository,
                        PointHistoryRepository pointHistoryRepository) {
        this.pointRepository = pointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public Point recordJoinBonus(User user){
        // 1. 포인트 계좌(잔액 0) 생성
        Point point = new Point(user);
        pointRepository.save(point);

        // 2. 포인트 히스토리 기록
        PointHistory history = new PointHistory(user, 0L, 0L, PointType.JOIN);
        pointHistoryRepository.save(history);
        return point;
    }

    public Point getUserPints(Long userId){
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("포인트가 없습니다. userId=" + userId));
    }
}
