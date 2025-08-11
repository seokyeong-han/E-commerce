package com.example.ecommerce.coupon.application.dto;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.model.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "쿠폰 생성 응답 DTO")
public class CreateCouponResponse {
    @Schema(description = "쿠폰 ID", example = "1")
    private Long id;

    @Schema(description = "쿠폰명", example = "웰컴쿠폰")
    private String name;

    @Schema(description = "할인 타입", example = "FIXED")
    private DiscountType type;

    @Schema(description = "총 발급 수량", example = "100")
    private int totalQuantity;

    @Schema(description = "만료일시", example = "2025-12-31T23:59:59")
    private LocalDateTime expiredAt;

    public static CreateCouponResponse from(Coupon coupon) {
        return new CreateCouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getType(),
                coupon.getTotalQuantity(),
                coupon.getExpiredAt()
        );
    }
}
