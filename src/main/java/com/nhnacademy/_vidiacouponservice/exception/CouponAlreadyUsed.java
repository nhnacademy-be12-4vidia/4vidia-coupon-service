package com.nhnacademy._vidiacouponservice.exception;

public class CouponAlreadyUsed extends RuntimeException {
    public CouponAlreadyUsed(Long couponId) {
        super("쿠폰("+ couponId + ")이 이미 사용되었습니다.");
    }
}
