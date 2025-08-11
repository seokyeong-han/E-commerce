package com.example.ecommerce.coupon.infrastructure;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.model.DiscountType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon")
public class CouponJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DiscountType type;

    @Column
    private Long discountAmount;

    @Column
    private Integer discountRate;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer issuedQuantity;

    @Column(name = "expired_at", nullable = false, updatable = false)
    private LocalDateTime expiredAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Coupon toDomain(){
        return Coupon.builder()
                .id(this.id)
                .name(this.name)
                .type(this.type)
                .discountAmount(this.discountAmount)
                .discountRate(this.discountRate)
                .totalQuantity(this.totalQuantity)
                .issuedQuantity(this.issuedQuantity)
                .expiredAt(this.expiredAt)
                .createdAt(this.createdAt)
                .build();
    }

    public static CouponJpaEntity fromDomain(Coupon coupon){
        return CouponJpaEntity.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .type(coupon.getType())
                .discountAmount(coupon.getDiscountAmount())
                .discountRate(coupon.getDiscountRate())
                .totalQuantity(coupon.getTotalQuantity())
                .issuedQuantity(coupon.getIssuedQuantity())
                .expiredAt(coupon.getExpiredAt())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
}
