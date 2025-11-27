package com.nhnacademy._vidiacouponservice.exception;

public class PolicyNotFoundException extends RuntimeException {
    public PolicyNotFoundException(Long id) {
        super("정책을 찾을 수 없음: id=" + id);
    }
}
