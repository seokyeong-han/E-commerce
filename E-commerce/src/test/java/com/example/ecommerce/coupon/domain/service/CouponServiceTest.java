package com.example.ecommerce.coupon.domain.service;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.model.CouponHistory;
import com.example.ecommerce.coupon.domain.model.CouponCommand;
import com.example.ecommerce.coupon.domain.model.DiscountType;
import com.example.ecommerce.coupon.domain.repository.CouponHistoryRepository;
import com.example.ecommerce.coupon.domain.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

// 1. JUnit5에게 Mockito 확장 기능을 사용하라고 알려줍니다.
@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    // 2. Mockito.mock(...)을 대체하는 어노테이션입니다.
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @Test
    void 쿠폰_생성_성공_히스토리_기록() {
        // given
        var cmd = new CouponCommand.Create(
                "웰컴쿠폰",
                DiscountType.FIXED,
                1000L,
                null,
                100,
                LocalDateTime.now() ,
                LocalDateTime.now().plusDays(30));

        LocalDateTime from = LocalDateTime.now();
        /*
        given 으로 couponRepository저장할때 any(Coupon.class): 어떤 Coupon
        객체가 들어와도 매칭 .willAnswer(...)로 지정한 동작을 수행
        */
        given(couponRepository.save(any(Coupon.class)))
                .willAnswer(inv -> {
                    Coupon c = inv.getArgument(0);
                    return Coupon.builder()
                            .id(1L)
                            .name(c.getName())
                            .type(c.getType())
                            .discountAmount(c.getDiscountAmount())
                            .discountRate(c.getDiscountRate())
                            .totalQuantity(c.getTotalQuantity())
                            .issuedQuantity(c.getIssuedQuantity())
                            .createdAt(c.getCreatedAt())
                            .activeFrom(c.getActiveFrom())
                            .expiredAt(c.getExpiredAt())
                            .build();
                });

        given(couponHistoryRepository.save(any(CouponHistory.class)))
                .willAnswer(inv -> inv.getArgument(0));
        // when
        Coupon saved = couponService.createCoupon(cmd);

        // then
        assertEquals(1L, saved.getId());
        assertEquals("웰컴쿠폰", saved.getName());
        // from과 saved.getActiveFrom()의 시간을 초(Second) 단위에서 절삭하여 비교
        assertEquals(from.truncatedTo(ChronoUnit.SECONDS),
                saved.getActiveFrom().truncatedTo(ChronoUnit.SECONDS));

    }



}