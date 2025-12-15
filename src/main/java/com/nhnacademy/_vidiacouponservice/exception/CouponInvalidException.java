package com.nhnacademy._vidiacouponservice.exception;

public class CouponInvalidException extends RuntimeException {
    public CouponInvalidException(Long couponId, String reason) {
        super("쿠폰(" + couponId + ") 사용 불가: " + reason);
    }
}
