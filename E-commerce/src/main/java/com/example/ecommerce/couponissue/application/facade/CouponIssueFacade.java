package com.example.ecommerce.couponissue.application.facade;

import com.example.ecommerce.couponissue.application.dto.IssueCouponRequest;
import com.example.ecommerce.couponissue.application.dto.IssueCouponResponse;
import com.example.ecommerce.couponissue.domain.model.CouponIssueCommand;
import com.example.ecommerce.couponissue.domain.model.UserCoupon;
import org.springframework.stereotype.Component;

@Component
public class CouponIssueFacade {

    public IssueCouponResponse issue(Long couponId, IssueCouponRequest req) {
        //dto -> command
        CouponIssueCommand.Issue command = new CouponIssueCommand.Issue(
                req.getUserId(),
                couponId);

        //쿠폰발급
        UserCoupon userCoupon = null;//CouponIssueService.issue(command);

        return IssueCouponResponse.of(userCoupon);
    }
}
