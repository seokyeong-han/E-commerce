package com.example.ecommerce.point.domain.service;

import com.example.ecommerce.point.domain.model.Point;
import com.example.ecommerce.point.domain.model.PointHistory;
import com.example.ecommerce.point.domain.model.PointType;
import com.example.ecommerce.point.domain.repository.PointHistoryRepository;
import com.example.ecommerce.point.domain.repository.PointRepository;
import com.example.ecommerce.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {
    private PointRepository pointRepository;
    private PointHistoryRepository pointHistoryRepository;
    private PointService pointService;

    @BeforeEach
    void setUp() {
        pointRepository = mock(PointRepository.class);
        pointHistoryRepository = mock(PointHistoryRepository.class);
        pointService = new PointService(pointRepository, pointHistoryRepository);
    }

    @Test
    void recordJoinBonus_정상_저장_검증() {
        // given
        User user = new User(1L, "testUser", LocalDateTime.now(),LocalDateTime.now());

        // when
        Point result = pointService.recordJoinBonus(user);

        // then - PointRepository.save 호출 확인
        ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
        verify(pointRepository, times(1)).save(pointCaptor.capture());
        Point savedPoint = pointCaptor.getValue();

        assertThat(savedPoint.getUserId()).isEqualTo(user.getId());
        assertThat(savedPoint.getBalance()).isEqualTo(0L);

        // then - PointHistoryRepository.save 호출 확인
        ArgumentCaptor<PointHistory> historyCaptor = ArgumentCaptor.forClass(PointHistory.class);
        verify(pointHistoryRepository, times(1)).save(historyCaptor.capture());
        PointHistory savedHistory = historyCaptor.getValue();

        assertThat(savedHistory.getUserId()).isEqualTo(user.getId());
        assertThat(savedHistory.getAmount()).isEqualTo(0L);
        assertThat(savedHistory.getType()).isEqualTo(PointType.JOIN);

        // 반환값 검증
        assertThat(result.getBalance()).isEqualTo(0L);

    }


}