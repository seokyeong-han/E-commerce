package com.example.ecommerce.coupon.domain.service;

import com.example.ecommerce.coupon.application.facade.CouponFacade;
import com.example.ecommerce.coupon.infrastructure.CouponIssueScheduler;
import com.example.ecommerce.couponissue.infrastructure.redis.RedisKeys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@Testcontainers //TectContainer 활성화
@SpringBootTest
class CouponQueueConsumerTest {
    //쿠폰 대기열 등록 및 분할 처리 test

    @Container
    @ServiceConnection // DataSource 자동 구성
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.2.4-alpine")
            .withExposedPorts(6379);

    // Testcontainers의 동적 포트를 스프링 설정에 직접 주입하는 메서드
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private CouponRedisService couponRedisService;

    @SuppressWarnings("deprecation") //경고 무시
    @MockBean
    private CouponFacade couponFacade;

    @Autowired
    private CouponIssueScheduler couponIssueScheduler; // 테스트할 대상

    @Autowired
    private RedissonClient redissonClient;// 실제 Redis에 데이터를 넣기 위해 주입

    // 테스트용 쿠폰 ID
    private final Long couponId1 = 1L;
    private final Long userId1 = 100L;
    private final Long couponId2 = 2L;
    private final Long userId2 = 200L;

    @BeforeEach
    void setUp() {
        // 유효기간 검증을 통과하도록 설정 (현재 시간보다 미래로)
        long futureTime = System.currentTimeMillis() + 1000000;

        // given (준비) - 'validateCouponExists'가 통과할 수 있도록
        // 테스트 시작 전에 유효한 쿠폰 메타데이터를 Redis에 미리 넣어둡니다.
        RMap<String, String> couponMeta = redissonClient.getMap(RedisKeys.meta(couponId1));
        couponMeta.put("id", String.valueOf(couponId1));
        couponMeta.put("totalQuantity", "1000");
        couponMeta.put("endTime", String.valueOf(futureTime));

        RMap<String, String> couponMeta2 = redissonClient.getMap(RedisKeys.meta(couponId2));
        couponMeta2.put("id", String.valueOf(couponId2));
        couponMeta2.put("totalQuantity", "500"); // 예시 데이터
        couponMeta2.put("endTime", String.valueOf(futureTime));
    }

    @AfterEach
    void tearDown() {
        // 각 테스트가 끝난 후, Redis를 깨끗하게 비웁니다.
        redissonClient.getKeys().flushall();
    }

    @Test
    @DisplayName("유효한 쿠폰의 발급 요청을 대기열에 성공적으로 추가한다")
    void addCouponIssueRequestToQueue_Success() {
        // when (실행)
        couponRedisService.addCouponIssueRequestToQueue(couponId1, userId1);

        // then (검증)
        // 1. Redis의 'issueQueue'에 데이터가 1개 들어갔는지 확인
        RList<String> queue = redissonClient.getList(RedisKeys.issueQueue());
        assertThat(queue.size()).isEqualTo(1);

        // 2. 큐에 들어간 데이터가 "1:100" 형식으로 올바르게 들어갔는지 확인
        assertThat(queue.get(0)).isEqualTo(couponId1 + ":" + userId1);
    }

    @Test
    @DisplayName("대기열에 2개의 요청이 있으면, Facade를 2번 호출한다")
    void processQueue_Success() {
        // given (준비)
        // 1. 실제 Redis 큐에 2개의 테스트 데이터를 넣습니다.
        couponRedisService.addCouponIssueRequestToQueue(couponId1, userId1); // "1:100"
        couponRedisService.addCouponIssueRequestToQueue(couponId2, userId2); // "2:200"

        RList<String> initialQueue = redissonClient.getList(RedisKeys.issueQueue());
        assertThat(initialQueue.size()).isEqualTo(2); // 데이터 2개 확인

        // when (실행)
        // 스케줄러 로직 실행 (이제 에러 없이 Facade 호출까지 진행될 것)
        couponIssueScheduler.processQueue();

        // then (검증)
        // CouponFacade가 호출되었는지 검증하는 코드를 여기에 추가해야 함
        verify(couponFacade, times(1)).processCouponIssuance(couponId1, userId1);
        verify(couponFacade, times(1)).processCouponIssuance(couponId2, userId2);

        // 큐가 비워졌는지 확인
        RList<String> finalQueue = redissonClient.getList(RedisKeys.issueQueue());
        assertThat(finalQueue.size()).isZero();
    }
}