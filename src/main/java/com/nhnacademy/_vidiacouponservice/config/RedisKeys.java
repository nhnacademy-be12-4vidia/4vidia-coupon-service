package com.nhnacademy._vidiacouponservice.config;

public final class RedisKeys {

    private static final String PREFIX = "coupon"; // 필요하면 env별 prefix 지원

    private RedisKeys() {}

    /**
     * 재고 키 (단일 값)
     * 예: coupon:policy:10:stock
     */
    public static String stockKey(Long policyId) {
        return PREFIX + ":policy:" + policyId + ":stock";
    }

    /**
     * 중복 발급 방지 키 (SETNX)
     * 예: coupon:dup:10:123  (policyId=10, userId=123)
     */
    public static String dupIssueKey(Long policyId, Long userId) {
        return PREFIX + ":dup:" + policyId + ":" + userId;
    }

    /**
     * 정책 해시 키 (Redis Hash 구조)
     * 예: coupon:policy:10
     *
     * 필드 구조 예시:
     *  stock = 5000
     *  issued = 120
     *  minOrderAmount = 0
     *  maxDiscountAmount = 25000
     */
    public static String policyHash(Long policyId) {
        return PREFIX + ":policy:" + policyId;
    }

    /**
     * 웰컴/생일 쿠폰 등 정책 타입별 발급 키(필요 시)
     * 예: coupon:welcome:123
     */
    public static String welcomeDupKey(Long userId) {
        return PREFIX + ":welcome:" + userId;
    }

    /**
     * 발급 완료 카운트 (정책별)
     * 예: coupon:issued:10 → 1234
     */
    public static String issuedCountKey(Long policyId) {
        return PREFIX + ":issued:" + policyId;
    }
}
