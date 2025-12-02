package com.nhnacademy._vidiacouponservice.exception;

public class CouponNotHoldException extends RuntimeException {

    public CouponNotHoldException(Long couponId) {
        super("유저가 쿠폰(" + couponId + ")을 가지고 있지 않습니다.");
    }
}
