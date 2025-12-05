package com.nhnacademy._vidiacouponservice.exception;

public class CouponExpireException extends RuntimeException {
    public CouponExpireException(Long policyId) {
        super("쿠폰(" + policyId + ")이 만료됨");
    }
}
