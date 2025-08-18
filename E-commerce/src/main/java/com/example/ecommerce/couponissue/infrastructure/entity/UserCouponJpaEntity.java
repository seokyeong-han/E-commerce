package com.example.ecommerce.couponissue.infrastructure.entity;

import com.example.ecommerce.couponissue.domain.model.UserCoupon;
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
@Table(name = "user_coupons")
public class UserCouponJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long couponId;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @PrePersist
    protected void onCreate() { this.issuedAt = LocalDateTime.now(); }

    public UserCoupon toDomain(){
        return UserCoupon.builder()
                .id(this.id)
                .userId(this.userId)
                .couponId(this.couponId)
                .used(this.used)
                .issuedAt(this.issuedAt)
                .build();
    }

    public static UserCouponJpaEntity fromDomain(UserCoupon userCoupon){
        return UserCouponJpaEntity.builder()
                .id(userCoupon.getId())
                .userId(userCoupon.getUserId())
                .couponId(userCoupon.getCouponId())
                .used(userCoupon.isUsed())
                .issuedAt(userCoupon.getIssuedAt())
                .build();
    }
}
