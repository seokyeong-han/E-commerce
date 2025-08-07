package com.example.ecommerce.point.integration;

import com.example.ecommerce.point.domain.model.Point;
import com.example.ecommerce.point.domain.model.PointCommand;
import com.example.ecommerce.point.domain.repository.PointRepository;
import com.example.ecommerce.point.domain.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PointIntegrationTest {
    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointService pointService;

    @BeforeEach
    void setUp() {

        //초기 포인트 등록
        Point point = Point.builder()
                .userId(1L)
                .balance(1000L)
                .build();
        pointRepository.save(point);
    }

    @Test
    void 포인트_충전_통합_테스트() throws InterruptedException {
        //동시에 10번 포인트 충전
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long userId = 1L;

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    pointService.charge(new PointCommand.Charge(userId, 100L));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Point result = pointRepository.findByUserId(userId).orElseThrow();
        System.out.println("최종 잔액: " + result.getBalance());
        assertEquals(1000L + 100L * threadCount, result.getBalance());
    }
}
