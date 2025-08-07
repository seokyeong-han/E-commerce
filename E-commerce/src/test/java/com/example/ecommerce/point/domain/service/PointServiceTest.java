package com.example.ecommerce.point.domain.service;

import com.example.ecommerce.point.domain.model.Point;
import com.example.ecommerce.point.domain.model.PointCommand;
import com.example.ecommerce.point.domain.model.PointHistory;
import com.example.ecommerce.point.domain.model.PointType;
import com.example.ecommerce.point.domain.repository.PointHistoryRepository;
import com.example.ecommerce.point.domain.repository.PointRepository;
import com.example.ecommerce.user.domain.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.ecommerce.point.domain.model.PointType.CHARGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class PointServiceTest {
    private PointRepository pointRepository;
    private PointHistoryRepository pointHistoryRepository;
    private PointService pointService;
    private PointHistoryService pointHistoryService;

    @BeforeEach
    void setUp() {
        pointRepository = mock(PointRepository.class);
        pointHistoryRepository = mock(PointHistoryRepository.class);
        pointService = new PointService(pointRepository, pointHistoryRepository);
        pointHistoryService = new PointHistoryService(pointHistoryRepository);
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

    @Test
    void 유저포인트조회_성공(){
        // given
        User user = new User(1L, "testUser", LocalDateTime.now(),LocalDateTime.now());
        Point point = Point.builder()
                .id(1L)
                .userId(user.getId())
                .balance(1000L)
                .build();
        // Mock repository가 userId로 조회했을 때 point를 반환하도록 설정
        given(pointRepository.findByUserId(user.getId()))
                .willReturn(Optional.of(point));
        // when
        Point result = pointService.getUserPints(user.getId());
        // then
        assertNotNull(result);
        assertEquals(1000L, result.getBalance());
        assertEquals(user.getId(), result.getUserId());
    }

    @Test
    void 유저포인트조회_실패_데이터없음() {
        // given
        Long userId = 2L;
        given(pointRepository.findByUserId(userId)).willReturn(Optional.empty());

        // when & then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> pointService.getUserPints(userId)
        );
        assertTrue(exception.getMessage().contains("포인트가 없습니다. userId=" + userId));
    }

    @Test
    void 포인트_충전_성공(){
        // given
        User user = new User(1L, "testUser", LocalDateTime.now(),LocalDateTime.now());
        Point point = Point.builder()
                .id(1L)
                .userId(user.getId())
                .balance(1000L)
                .build();

        // mock: 포인트 조회
        given(pointRepository.findByUserId(user.getId()))
                .willReturn(Optional.of(point));

        // mock: 포인트 저장 시, 저장된 객체 그대로 반환
        given(pointRepository.save(any(Point.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //충전
        PointCommand.Charge command = new PointCommand.Charge(user.getId(), 100L);
        Point chargePoint = pointService.charge(command);

        //히스토리 저장
        PointHistory savedPointHis = pointHistoryService.saveChargeHistory(chargePoint.getUserId(), 100L, chargePoint.getBalance());
        given(pointHistoryRepository.save(any(PointHistory.class)))
                .willAnswer(invocation -> invocation.getArgument(0));


        //point 검증
        assertEquals(1100L, chargePoint.getBalance());
        //pointHistory 검증
        assertEquals(user.getId(), savedPointHis.getUserId());
        assertEquals(100L, savedPointHis.getAmount());
        assertEquals(CHARGE, savedPointHis.getType());
    }

    @Test
    void 포인트_충전_실패_음수충전(){
        // given
        User user = new User(1L, "testUser", LocalDateTime.now(),LocalDateTime.now());
        Point point = Point.builder()
                .id(1L)
                .userId(user.getId())
                .balance(1000L)
                .build();

        // mock: 포인트 조회
        given(pointRepository.findByUserId(user.getId()))
                .willReturn(Optional.of(point));

        // mock: 포인트 저장 시, 저장된 객체 그대로 반환
        given(pointRepository.save(any(Point.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //충전
        PointCommand.Charge command = new PointCommand.Charge(user.getId(), -100L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pointService.charge(command);
        });
        assertEquals("충전 금액은 0보다 커야 합니다.", exception.getMessage());
    }

}