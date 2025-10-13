package com.example.ecommerce.coupon.domain.service;

import com.example.ecommerce.couponissue.infrastructure.redis.RedisKeys;
import jakarta.persistence.EntityNotFoundException;

import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CouponRedisService {
    private final RedissonClient redisson;

    public CouponRedisService(
            ApplicationEventPublisher eventPublisher
            , RedissonClient redisson) {
        this.redisson = redisson;
    }

    /**
     * 쿠폰 발급 요청을 대기열에 추가하는 비즈니스 로직을 수행합니다.
     */
    public void addCouponIssueRequestToQueue(Long couponId, Long userId) {
        validateCouponExists(couponId); //쿠폰 검증

        RList<String> queue = redisson.getList(RedisKeys.issueQueue());
        queue.add(couponId + ":" + userId);
    }

    public List<String> popRequestsFromQueue(long batchSize) {
        // 1. '깃발'의 이름을 정합니다.
        RLock lock = redisson.getLock("couponIssueQueueLock");
        List<String> requests = null;
        try {
            // 2. 깃발을 꽂으려고 시도합니다. 최대 5초간 기다리고, 깃발을 꽂으면 10초간 유효합니다.
            boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);

            if (isLocked) {
                // 3. 깃발을 꽂는 데 성공했다면, '나 혼자' 안전하게 작업을 수행합니다.
                RList<String> queue = redisson.getList(RedisKeys.issueQueue());
                // 존재하는 range와 trim을 사용하지만, 락 때문에 안전합니다.
                requests = queue.range(0, (int) batchSize - 1);
                queue.trim((int) batchSize, -1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 락을 기다리다 인터럽트 발생 시 로그 남기기
        } finally {
            // 4. 내 작업이 끝났으면, 다른 사람이 쓸 수 있도록 반드시 깃발을 뽑습니다.
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return requests != null ? requests : List.of(); // null 대신 빈 리스트 반환
    }

    /**
     * 쿠폰의 존재 여부를 검증합니다.
     */
    public void validateCouponExists(Long couponId) {
        RMap<String, String> couponMeta = redisson.getMap(RedisKeys.meta(couponId));
        //쿠폰 존재 여부 확인(TTL 만료)
        if (couponMeta.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 쿠폰입니다.");
        }

        //쿠폰 유효기간 검증
        String endTimeStr = couponMeta.get("endTime");
        long currentTimeMillis = Instant.now()
                .atZone(ZoneId.of("Asia/Seoul")) // 저장 시 사용한 ZoneId와 동일하게 사용
                .toInstant()
                .toEpochMilli();

        if (endTimeStr != null) {
            try {
                long expireTimeMillis = Long.parseLong(endTimeStr);

                // 현재 시간이 저장된 만료 시간보다 미래인지 확인
                if (currentTimeMillis >= expireTimeMillis) {
                    // 만료 시간을 지났다면 예외 발생
                    throw new IllegalStateException("쿠폰의 사용 기간이 만료되었습니다.");
                }

                // 보너스: 시작 시간도 함께 검증하여 아직 사용 개시 전인 쿠폰도 처리
                String startTimeStr = couponMeta.get("startTime");
                if (startTimeStr != null) {
                    long startTimeMillis = Long.parseLong(startTimeStr);
                    if (currentTimeMillis < startTimeMillis) {
                        throw new IllegalStateException("쿠폰의 사용 시작일 이전입니다.");
                    }
                }

            } catch (NumberFormatException e) {
                // Redis에 저장된 endTime 값이 숫자가 아닐 경우의 예외 처리
                throw new RuntimeException("쿠폰 메타데이터의 시간 형식이 잘못되었습니다.", e);
            }
        }

    }
}
