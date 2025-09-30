package com.example.ecommerce.coupon.domain.service;

import com.example.ecommerce.coupon.domain.model.Coupon;
import com.example.ecommerce.coupon.domain.model.CouponCommand;
import com.example.ecommerce.coupon.domain.model.CouponHistory;
import com.example.ecommerce.coupon.domain.repository.CouponHistoryRepository;
import com.example.ecommerce.coupon.domain.repository.CouponRepository;
import com.example.ecommerce.couponissue.domain.event.CouponCreatedEvent;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CouponService {
    private final ApplicationEventPublisher eventPublisher;
    private final CouponRepository couponRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    public CouponService(
            ApplicationEventPublisher eventPublisher
            ,CouponRepository couponRepository
            ,CouponHistoryRepository couponHistoryRepository) {
        this.eventPublisher = eventPublisher;
        this.couponRepository = couponRepository;
        this.couponHistoryRepository = couponHistoryRepository;
    }

    @Transactional
    public Coupon createCoupon(CouponCommand.Create command) {
        //쿠폰생성 DB 저장
        Coupon coupon = Coupon.create(command);
        Coupon saved = couponRepository.save(coupon);
        //쿠폰발급 DB 히스토리 저장
        couponHistoryRepository.save(CouponHistory.created(saved));

        //DB 트랙젝션 성공, 커밋 성공 후 이벤트 발행 예약
        eventPublisher.publishEvent(new CouponCreatedEvent(saved.getId()));

        return saved;
    }
}
