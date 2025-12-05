package com.nhnacademy._vidiacouponservice.exception;

public class CouponAlreadyUsedException extends RuntimeException {
    public CouponAlreadyUsedException(Long couponId) {
        super("쿠폰("+ couponId + ")이 이미 사용되었습니다.");
    }
}
