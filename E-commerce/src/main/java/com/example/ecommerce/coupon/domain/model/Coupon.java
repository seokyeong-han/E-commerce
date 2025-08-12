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
        Long amount = 0L;
        int rate = 0;

        // 공통 검증
        if (command.getTotalQuantity() == null || command.getTotalQuantity() <= 0)
            throw new IllegalArgumentException("총 발급 수량은 1 이상이어야 합니다.");
        if (command.getExpiredAt() == null)
            throw new IllegalArgumentException("만료일시는 필수입니다.");

        // 타입별 검증
        if (command.getType() == DiscountType.FIXED) {
            if (command.getDiscountAmount() == null || command.getDiscountAmount() <= 0)
                throw new IllegalArgumentException("고정 할인 금액은 1 이상이어야 합니다.");
            amount = command.getDiscountAmount();
            rate = 0;

        } else if (command.getType() == DiscountType.RATE) {
            if (command.getDiscountRate() == null || command.getDiscountRate() <= 0 || command.getDiscountRate() > 100)
                throw new IllegalArgumentException("할인 비율은 1~100 사이여야 합니다.");
            rate = command.getDiscountRate();
            amount = 0L;
        } else {
            throw new IllegalArgumentException("잘못된 할인 타입");
        }

        return Coupon.builder()
                .name(command.getName())
                .type(command.getType())
                .discountAmount(amount)
                .discountRate(rate)
                .totalQuantity(command.getTotalQuantity())
                .issuedQuantity(0)
                .expiredAt(command.getExpiredAt())
                .build();
    }
}
