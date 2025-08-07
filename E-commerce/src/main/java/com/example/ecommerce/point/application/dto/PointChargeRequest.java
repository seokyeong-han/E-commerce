package com.example.ecommerce.point.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointChargeRequest {
    @Schema(description = "유저ID", example = "1")
    private Long userId;
    @Schema(description = "충전금액", example = "100")
    private Long amount; // 충전할 금액
}
