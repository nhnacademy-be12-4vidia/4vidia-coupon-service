package com.nhnacademy._vidiacouponservice.domain.dto.request;

public record CouponValidateRequest(
        Long userId,
        Long orderAmount,
        Long bookId,
        Long categoryId
) {}
