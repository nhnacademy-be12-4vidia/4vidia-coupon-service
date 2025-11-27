package com.nhnacademy._vidiacouponservice.exception;

public class PolicyInactiveException extends RuntimeException {
    public PolicyInactiveException(Long id) {
        super("비활성화된 정책입니다. id=" + id);
    }
}
