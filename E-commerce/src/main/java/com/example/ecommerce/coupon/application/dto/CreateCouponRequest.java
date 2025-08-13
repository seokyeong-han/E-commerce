package com.example.ecommerce.coupon.application.dto;

import com.example.ecommerce.coupon.domain.model.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "쿠폰 생성 요청 DTO")
public class CreateCouponRequest {
    @Schema(description = "쿠폰명", example = "웰컴쿠폰")
    private String name;
    @Schema(description = "할인 타입 (FIXED: 고정할인, RATE: 비율할인)", example = "FIXED")
    private DiscountType type;     // FIXED or RATE
    @Schema(description = "할인 금액 (type이 FIXED일 경우 필수)", example = "1000")
    private Long discountAmount;   // 고정 금액
    @Schema(description = "할인 비율 (type이 RATE일 경우 필수)", example = "10")
    private Integer discountRate;  // 비율
    @Schema(description = "총 발급 수량", example = "100")
    private Integer totalQuantity;
    @Schema(description = "쿠폰 발급 시작 일시", example = "2025-12-30T23:00:00")
    private LocalDateTime activeFrom;
    @Schema(description = "쿠폰 만료일시", example = "2025-12-31T23:59:59")
    private LocalDateTime expiredAt;
}
