package com.example.ecommerce.couponissue.domain.service;

import com.example.ecommerce.coupon.domain.repository.CouponRepository;
import com.example.ecommerce.couponissue.domain.repository.CouponIssueHistoryRepository;
import com.example.ecommerce.couponissue.domain.repository.UserCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //@Mock 필드들을 MockitoExtension이 자동 초기화
class CouponIssueServiceTest {
    @Mock RedissonClient redisson;
    @Mock RLock rLock;
    @Mock CouponRepository couponRepository;
    @Mock UserCouponRepository userCouponRepository;
    @Mock CouponIssueHistoryRepository historyRepository;

    CouponIssueService service;

    @BeforeEach //테스트 메서드 실행 전에 매번 실행되는 초기화 메서드
    void setUp() {
        service = new CouponIssueService(redisson, couponRepository, userCouponRepository, historyRepository);
        when(redisson.getLock(anyString())).thenReturn(rLock);
        //실제 Redisson의 getLock() 메서드를 타지 않고, 테스트 전용 락(Mock RLock)을 돌려줍니다.
        //→ 덕분에 Redis 서버가 없어도 테스트할 수 있고, 원하는 락 동작을 rLock Mock으로 컨트롤할 수 있습니다.
    }

    @Test
    void 쿠폰_발급_성공(){

    }



}