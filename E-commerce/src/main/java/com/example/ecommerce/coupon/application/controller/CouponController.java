package com.example.ecommerce.coupon.application.controller;

import com.example.ecommerce.coupon.application.dto.CreateCouponRequest;
import com.example.ecommerce.coupon.application.dto.CreateCouponResponse;
import com.example.ecommerce.coupon.application.facade.CouponFacade;
import com.example.ecommerce.docs.dto.CommApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/coupons")
public class CouponController {
    private final CouponFacade couponFacade;

    public CouponController(CouponFacade couponFacade){
        this.couponFacade = couponFacade;
    }

    @Operation(summary = "쿠폰발급", description = "쿠폰을 생성합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "쿠폰 발급 성공",
            content = @Content(schema = @Schema(implementation = CreateCouponResponse.class))
    )
    @PostMapping
    public ResponseEntity<CommApiResponse<CreateCouponResponse>> createCoupon(@RequestBody CreateCouponRequest req){
        CreateCouponResponse response = couponFacade.createCoupon(req);
        return ResponseEntity.ok(CommApiResponse.success(response));
    }
}
