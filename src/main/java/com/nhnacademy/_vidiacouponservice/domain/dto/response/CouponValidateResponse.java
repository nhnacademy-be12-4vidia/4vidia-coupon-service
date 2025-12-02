package com.nhnacademy._vidiacouponservice.domain.dto.response;

public record CouponValidateResponse(
        boolean isValid,
        String reason,
        String discountType,
        Integer discountValue,
        Integer maxDiscountAmount,
        String discountTargetType,
        Long categoryId,
        Long bookId,
        Integer minOrderAmount,
        String expireAt
) {}
