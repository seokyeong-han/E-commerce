package com.example.ecommerce.couponissue.application.facade;

import com.example.ecommerce.couponissue.application.dto.IssueCouponRequest;
import com.example.ecommerce.couponissue.application.dto.IssueCouponResponse;
import com.example.ecommerce.couponissue.domain.model.CouponIssueCommand;
import com.example.ecommerce.couponissue.domain.model.UserCoupon;
import com.example.ecommerce.couponissue.domain.service.CouponIssueService;
import org.springframework.stereotype.Component;

@Component
public class CouponIssueFacade {
    private final CouponIssueService couponIssueService;

    public CouponIssueFacade(CouponIssueService couponIssueService) {
        this.couponIssueService = couponIssueService;
    }

    public IssueCouponResponse issue(Long couponId, IssueCouponRequest req) {
        //dto -> command
        CouponIssueCommand.Issue command = new CouponIssueCommand.Issue(
                req.getUserId(),
                couponId);

        //쿠폰발급
        UserCoupon userCoupon = couponIssueService.issue(command);

        return IssueCouponResponse.of(userCoupon);
    }
}
