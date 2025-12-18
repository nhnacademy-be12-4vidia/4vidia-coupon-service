package com.nhnacademy._vidiacouponservice.domain.dto.response;

public record UseCouponResponse (
    String discountTargetType, // ALL, CATEGORY, BOOK
    String categoryKdcId,
    Long bookId,
    Integer minOrderAmount
) {}
