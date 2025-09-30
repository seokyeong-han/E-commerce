package com.example.ecommerce.couponissue.infrastructure.redis;

public final class RedisKeys {
    private RedisKeys() {}
    //coupon key
    public static String issued(long cid){ return "coupon:"+cid+":issued"; }
    public static String users(long cid){ return "coupon:"+cid+":users"; }
    public static String limit(long cid){ return "coupon:"+cid+":limit"; }

    /**
     * 쿠폰의 메타데이터(수량, 시작/종료 시간 등)를 저장하는 Hash의 Key
     * 예: coupon:meta:123
     */
    public static String meta(long cid) { return "coupon:meta:" + cid; }

}