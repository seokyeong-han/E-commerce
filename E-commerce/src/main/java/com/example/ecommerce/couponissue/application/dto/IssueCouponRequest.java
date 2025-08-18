package com.example.ecommerce.couponissue.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueCouponRequest {
    @Schema(description = "유저 ID", example = "1")
    private Long userId;
    /*@Schema(description = "쿠폰 ID", example = "10")
    private Long couponId;*/
}
