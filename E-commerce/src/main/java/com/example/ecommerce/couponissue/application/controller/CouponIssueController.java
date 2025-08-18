package com.example.ecommerce.couponissue.application.controller;

import com.example.ecommerce.coupon.application.dto.CreateCouponResponse;
import com.example.ecommerce.couponissue.application.dto.IssueCouponRequest;
import com.example.ecommerce.couponissue.application.dto.IssueCouponResponse;
import com.example.ecommerce.couponissue.application.facade.CouponIssueFacade;
import com.example.ecommerce.docs.dto.CommApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/coupons")
public class CouponIssueController {
    private final CouponIssueFacade  couponIssueFacade;

    public  CouponIssueController(CouponIssueFacade couponIssueFacade) {
        this.couponIssueFacade = couponIssueFacade;
    }

    @Operation(summary = "사용자 쿠폰발급", description = "선착순으로 쿠폰을 발급합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 쿠폰 발급 성공",
            content = @Content(schema = @Schema(implementation = IssueCouponResponse.class))
    )
    @PostMapping("/{couponId}/issue")
    public ResponseEntity<CommApiResponse<IssueCouponResponse>> issue(
            @PathVariable("couponId") Long couponId,
            @RequestBody IssueCouponRequest req) {

        IssueCouponResponse res = couponIssueFacade.issue(couponId, req);

        return ResponseEntity.ok(CommApiResponse.success(res));
    }
}
