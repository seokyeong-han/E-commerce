package com.example.ecommerce.coupon.domain.model;

// 쿠폰 마스터 이력 타입
public enum CouponHistoryType {
    CREATED,         // 쿠폰 생성
    UPDATED,         // 정책 변경(이름, 할인정책, 수량 등)
    QUANTITY_CHANGED,// 수량 조정
    EXPIRED,         // 만료 처리
    DELETED          // 삭제/비활성화
}
