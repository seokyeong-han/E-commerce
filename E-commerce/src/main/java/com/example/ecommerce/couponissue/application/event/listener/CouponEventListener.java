package com.example.ecommerce.couponissue.application.event.listener;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.repository.CouponRepository;
import com.example.ecommerce.couponissue.domain.event.CouponCreatedEvent;
import com.example.ecommerce.couponissue.infrastructure.redis.RedisKeys;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Component
public class CouponEventListener {
    private final RedissonClient redisson;
    private final CouponRepository couponRepository;

    public CouponEventListener(
            ApplicationEventPublisher eventPublisher
            ,RedissonClient redisson
            ,CouponRepository couponRepository) {
        this.redisson = redisson;
        this.couponRepository = couponRepository;
    }

    @Async // 이벤트를 별도의 스레드에서 비동기적으로 처리
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) //DB 커밋이 완료된 후에만 실행
    public void handleCouponCreatedEvent(CouponCreatedEvent event) {
        //저장된 couponId 받아 DB 정보 조회
        Coupon coupon = couponRepository.findById(event.getCouponId())
                .orElseThrow(() -> new IllegalStateException("쿠폰을 찾을 수 없습니다."));

        //Redis 캐시 데이터 생성
        RMap<String, String> couponMeta = redisson.getMap(RedisKeys.meta(coupon.getId()));
        //Redis hash set
        couponMeta.putAll(Map.of(
                "limit", String.valueOf(coupon.getTotalQuantity()),
                "startTime", String.valueOf(coupon.getActiveFrom()
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toInstant()
                        .toEpochMilli()),
                "endTime", String.valueOf(coupon.getExpiredAt()
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toInstant()
                        .toEpochMilli())
        ));
        //Redis 만료시간 설정
        ZonedDateTime expiredZonedDateTime = coupon.getExpiredAt().atZone(ZoneId.of("Asia/Seoul"));
        long ttlSeconds = Duration.between(ZonedDateTime.now(), expiredZonedDateTime).toSeconds() + 86400;// 종료 후 하루 더 유지
        if (ttlSeconds > 0) {
            couponMeta.expire(Duration.ofSeconds(ttlSeconds));
        }

        // 만약 여기서 Redis 작업이 실패하더라도, 원래의 쿠폰 생성 트랜잭션에는 영향을 주지 않습니다.
        // (필요 시, 실패 로깅 및 재시도 로직 추가 가능)
    }
}
