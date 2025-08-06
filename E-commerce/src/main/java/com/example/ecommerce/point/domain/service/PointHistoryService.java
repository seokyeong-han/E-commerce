package com.example.ecommerce.point.domain.service;

import com.example.ecommerce.point.domain.model.PointHistory;
import com.example.ecommerce.point.domain.model.PointType;
import com.example.ecommerce.point.domain.repository.PointHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public PointHistory saveChargeHistory(Long userId, Long amount, Long balance ){
        PointHistory history = new PointHistory(userId, amount, balance, PointType.CHARGE);
        pointHistoryRepository.save(history);
        return history;
    }
}
