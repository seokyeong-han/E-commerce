package com.example.ecommerce.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CouponHistory {
    private Long id;
    private Long couponId;
    private CouponHistoryType type;
    private Long operatorId;      // 관리자 ID 등
    private String reason;        // 사유

    private LocalDateTime createdAt;

    public static CouponHistory created(Coupon coupon) {
        return CouponHistory.builder()
                .couponId(coupon.getId())
                .type(CouponHistoryType.CREATED)
                .operatorId(11L) //관리자 아이디 고정
                .reason("쿠폰 생성")
                .build();
    }

}
