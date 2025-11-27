package com.nhnacademy._vidiacouponservice.exception;

public class PolicyOutOfStockException extends RuntimeException {
    public PolicyOutOfStockException(Long id) {
        super("쿠폰 재고가 모두 소진되었습니다. id=" + id);
    }
}
