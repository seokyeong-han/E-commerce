package com.example.ecommerce.coupon.infrastructure;

import com.example.ecommerce.coupon.domain.model.CouponHistory;
import com.example.ecommerce.coupon.domain.model.CouponHistoryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_history")
public class CouponHistoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long couponId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponHistoryType type;

    @Column(nullable = false)
    private Long operatorId; // 관리자 ID

    @Column(length = 255)
    private String reason; // 사유

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public CouponHistory toDomain(){
        return CouponHistory.builder()
                .id(id)
                .couponId(couponId)
                .type(type)
                .operatorId(operatorId)
                .reason(reason)
                .createdAt(createdAt)
                .build();
    }

    public static CouponHistoryJpaEntity fromDomain(CouponHistory couponHistory){
        return CouponHistoryJpaEntity.builder()
                .id(couponHistory.getId())
                .couponId(couponHistory.getCouponId())
                .type(couponHistory.getType())
                .operatorId(couponHistory.getOperatorId())
                .reason(couponHistory.getReason())
                .createdAt(couponHistory.getCreatedAt())
                .build();
    }
}
