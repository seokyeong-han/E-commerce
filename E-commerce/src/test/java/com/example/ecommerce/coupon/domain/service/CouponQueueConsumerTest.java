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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @Mock // ✅ 3. 가짜 CouponFacade 만들기
    private CouponFacade couponFacade;

    @Autowired
    private CouponIssueScheduler couponIssueScheduler; // 테스트할 대상

    @Autowired
    private RedissonClient redissonClient;// 실제 Redis에 데이터를 넣기 위해 주입

    // 테스트용 쿠폰 ID
    private final Long validCouponId = 1L;
    private final Long invalidCouponId = 99L;
    private final Long userId = 100L;

    @BeforeEach
    void setUp() {
        // given (준비) - 'validateCouponExists'가 통과할 수 있도록
        // 테스트 시작 전에 유효한 쿠폰 메타데이터를 Redis에 미리 넣어둡니다.
        RMap<String, String> couponMeta = redissonClient.getMap(RedisKeys.meta(validCouponId));
        couponMeta.put("id", String.valueOf(validCouponId));
        couponMeta.put("totalQuantity", "1000");

        // 유효기간 검증을 통과하도록 설정 (현재 시간보다 미래로)
        long futureTime = System.currentTimeMillis() + 1000000;
        couponMeta.put("endTime", String.valueOf(futureTime));
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
        couponRedisService.addCouponIssueRequestToQueue(validCouponId, userId);

        // then (검증)
        // 1. Redis의 'issueQueue'에 데이터가 1개 들어갔는지 확인
        RList<String> queue = redissonClient.getList(RedisKeys.issueQueue());
        assertThat(queue.size()).isEqualTo(1);

        // 2. 큐에 들어간 데이터가 "1:100" 형식으로 올바르게 들어갔는지 확인
        assertThat(queue.get(0)).isEqualTo(validCouponId + ":" + userId);
    }

    @Test
    @DisplayName("대기열에 2개의 요청이 있으면, Facade를 2번 호출한다")
    void processQueue_Success() {

    }
}