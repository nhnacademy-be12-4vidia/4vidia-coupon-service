package com.nhnacademy._vidiacouponservice.config;

/**
 * Redis에서 사용되는 key 규칙 모음
 * prefix: coupon
 */
public final class RedisKeys {

    private static final String PREFIX = "coupon";

    private RedisKeys() {}

    /** 재고 키 */
    public static String stockKey(Long policyId) {
        return PREFIX + ":policy:" + policyId + ":stock";
    }

    /** 발급 중복방지 key */
    public static String dupKey(Long policyId, Long userId) {
        return PREFIX + ":dup:" + policyId + ":" + userId;
    }

    /** Welcome 전용 중복 */
    public static String welcomeDupKey(Long userId) {
        return PREFIX + ":welcome:" + userId;
    }

    /** 정책 Hash */
    public static String policyHash(Long policyId) {
        return PREFIX + ":policy:" + policyId;
    }

    /** 정책별 발급 카운터 */
    public static String issuedCount(Long policyId) {
        return PREFIX + ":issued:" + policyId;
    }
}
