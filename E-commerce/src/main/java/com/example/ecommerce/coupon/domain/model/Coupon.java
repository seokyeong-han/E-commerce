package com.example.ecommerce.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Coupon {
    private Long id;
    private String name;
    private DiscountType type;         // FIXED, RATE
    private Long discountAmount;       // 고정 할인 금액
    private int discountRate;          // 퍼센트 할인 비율

    private int totalQuantity;         // 총 발급 가능 수량
    private int issuedQuantity;        // 현재까지 발급된 수량

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public boolean canIssue() {
        return issuedQuantity < totalQuantity;
    }

    public void issue() {
        if (!canIssue()) throw new IllegalStateException("쿠폰 수량 초과");
        if (isExpired()) throw new IllegalStateException("만료된 쿠폰입니다.");
        this.issuedQuantity++;
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }

    public Long calculateDiscount(Long totalPrice) {
        if (type == DiscountType.FIXED) return discountAmount;
        if (type == DiscountType.RATE) return totalPrice * discountRate / 100;
        return 0L;
    }

    public static Coupon create(CouponCommand.Create command){
        return Coupon.builder()
                .name(command.getName())
                .type(command.getType())
                .discountAmount(command.getDiscountAmount())
                .discountRate(command.getDiscountRate())
                .totalQuantity(command.getTotalQuantity())
                .issuedQuantity(0)
                .expiredAt(command.getExpiredAt())
                .build();
    }
}
