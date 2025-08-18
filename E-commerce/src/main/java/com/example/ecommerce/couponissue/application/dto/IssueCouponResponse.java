package com.example.ecommerce.couponissue.application.dto;

import com.example.ecommerce.couponissue.domain.model.UserCoupon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class IssueCouponResponse {
    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @Schema(description = "쿠폰 ID", example = "100")
    private Long couponId;

    @Schema(description = "발급여부", example = "true")
    private boolean issued;

    public static IssueCouponResponse of(UserCoupon uc) {
        return IssueCouponResponse.builder()
                .userId(uc.getUserId())
                .couponId(uc.getCouponId())
                .issued(true)  // 저장된 UserCoupon이면 무조건 발급 성공
                .build();

    }
}
