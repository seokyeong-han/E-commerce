package com.example.ecommerce.couponissue.domain.service;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.model.CouponCommand;
import com.example.ecommerce.coupon.domain.model.DiscountType;
import com.example.ecommerce.coupon.domain.repository.CouponRepository;
import com.example.ecommerce.couponissue.domain.model.CouponIssueCommand;
import com.example.ecommerce.couponissue.domain.model.UserCoupon;
import com.example.ecommerce.couponissue.domain.repository.CouponIssueHistoryRepository;
import com.example.ecommerce.couponissue.domain.repository.UserCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //@Mock 필드들을 MockitoExtension이 자동 초기화
class CouponIssueServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CouponIssueServiceTest.class);

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
    void 쿠폰_발급_성공() throws InterruptedException {
        Long couponId = 1L;
        Long userId = 10L;

        var cmd = new CouponCommand.Create(
                "웰컴쿠폰",
                DiscountType.FIXED,
                1000L,
                null,                       // 퍼센트형이면 null, 정액형이면 1000L
                100,                        // 발급 한도
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30)
        );
        Coupon coupon = Coupon.create(cmd);

        log.info("couponId: {}, coupon한도: {}", coupon.getId(), coupon.getTotalQuantity());

        //스텁하지 않으면, rLock은 Mock 객체라 기본값(false)만 리턴하거나 null을 줄 수 있고, 그럼 바로 예외가 발생해버려서 테스트가 실패
        // lock을 테스트에서 true를 돌려주도록 설정
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        // 레포 스텁
        //DB 없는 단위 테스트에서도 save()가 정상 동작한 것처럼 만들기 위함
        //save() 했을 때 null 대신 내가 넣은 객체를 그대로 돌려줘
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(userCouponRepository.save(any(UserCoupon.class))).thenAnswer(inv -> inv.getArgument(0));
        when(historyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(couponRepository.save(any(Coupon.class))).thenAnswer(inv -> inv.getArgument(0));

        var command = new CouponIssueCommand.Issue(userId, couponId);
        // when 실제 서비스 메서드 호출
        UserCoupon issued = service.issue(command);

        // then
        assertNotNull(issued);
        assertEquals(userId, issued.getUserId());
        assertEquals(couponId, issued.getCouponId());
        assertEquals(1, coupon.getIssuedQuantity()); // 예: 잔여 수량 확인
        log.info("couponId: {}, coupon한도: {}, coupon발급갯수: {}"
                , coupon.getId()
                , coupon.getTotalQuantity()
                , coupon.getIssuedQuantity());
    }



}