package com.example.ecommerce.couponissue.domain.service;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.repository.CouponRepository;
import com.example.ecommerce.couponissue.domain.model.CouponIssueCommand;
import com.example.ecommerce.couponissue.domain.model.UserCoupon;
import com.example.ecommerce.couponissue.domain.repository.UserCouponRepository;
import jakarta.transaction.Transactional;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CouponIssueService {
    private final RedissonClient redisson;
    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponIssueService(RedissonClient redisson
            , CouponRepository couponRepository
            ,  UserCouponRepository userCouponRepository) {
        this.redisson = redisson;
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    //선착순 쿠폰 발급
    @Transactional
    public UserCoupon issue(CouponIssueCommand.Issue command){
        //final : 값이 이후에 바뀌지 않는다
        final Long userId   = command.getUserId();
        final Long couponId = command.getCouponId();

        final String lockKey = "lock:coupon:" + couponId;
        RLock lock = redisson.getLock(lockKey);

        try {
            //100ms 안에 락을 못 잡으면 바로 실패(예외 발생)
            //락을 잡았다면, 최대 3초 동안만 점유하고 자동 해제
            if (!lock.tryLock(100, 3000, TimeUnit.MILLISECONDS)) {
                throw new IllegalStateException("요청이 많습니다. 잠시 후 다시 시도해주세요.");
            }

            //쿠폰조회
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new IllegalStateException("쿠폰이 존재하지 않습니다."));

            //쿠폰 발급 및 검증 수량 증가
            coupon.issue();

            //유저 쿠폰 생성
            UserCoupon userCoupon = UserCoupon.issue(userId, couponId);
            userCouponRepository.save(userCoupon);

            //유저 쿠폰 히스토리 생성

            //쿠폰 수량 수정

        }catch (Exception ex){

        }
        return null;
    }

}
