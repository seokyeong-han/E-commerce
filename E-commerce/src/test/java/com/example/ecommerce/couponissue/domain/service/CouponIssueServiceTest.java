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
import static org.mockito.Mockito.*;

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

    @Test
    void 락_획득_실패면_예외() throws Exception {
        Long couponId = 1L, userId = 10L;

        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        var command = new CouponIssueCommand.Issue(userId, couponId);

        assertThrows(IllegalStateException.class, () -> service.issue(command));

        // 저장 로직 전혀 안 타야 함
        verify(userCouponRepository, never()).save(any());
        verify(historyRepository, never()).save(any());
        verify(couponRepository, never()).save(any());
        // 락도 못 잡았으니 unlock 호출되지 않아야 함
        verify(rLock, never()).unlock();
    }

    @Test
    void 재고_이미_소진된_쿠폰이면_issue에서_실패하고_언락() throws Exception {
        Long couponId = 1L, userId = 10L;

        // 총량 1로 생성
        var cmd = new CouponCommand.Create(
                "소진쿠폰", DiscountType.FIXED, 1000L, null,
                1, // 총량 1
                LocalDateTime.now(), LocalDateTime.now().plusDays(30)
        );
        Coupon coupon = Coupon.create(cmd);

        // 사전 발급 1회로 소진 상태 만들기
        coupon.issue(); // issuedQuantity == 1 이 되어 소진

        // 락 스텁
        when(redisson.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        // 레포 스텁
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        var command = new CouponIssueCommand.Issue(userId, couponId);

        // 도메인 issue()에서 예외 → 서비스에서 RuntimeException으로 래핑될 수 있음
        assertThrows(RuntimeException.class, () -> service.issue(command));

        // 저장류 호출 안 됨
        verify(userCouponRepository, never()).save(any());
        verify(historyRepository, never()).save(any());
        // 재고 소진으로 실패했으니 couponRepository.save도 안 타야 정상
        verify(couponRepository, never()).save(any());

        // 락은 잡았으니 반드시 해제
        verify(rLock, times(1)).unlock();
    }

}