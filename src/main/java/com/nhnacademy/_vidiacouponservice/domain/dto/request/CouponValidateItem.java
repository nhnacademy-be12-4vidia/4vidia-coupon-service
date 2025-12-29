package com.nhnacademy._vidiacouponservice.domain.dto.request;

public record CouponValidateItem(
        Long bookId,
        String categoryKdcId,
        int price,
        int quantity
) {}
