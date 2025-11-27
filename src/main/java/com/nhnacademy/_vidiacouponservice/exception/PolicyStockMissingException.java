package com.nhnacademy._vidiacouponservice.exception;

public class PolicyStockMissingException extends RuntimeException {
    public PolicyStockMissingException(Long id) {
        super("정책 재고 정보가 없습니다. id=" + id);
    }
}
