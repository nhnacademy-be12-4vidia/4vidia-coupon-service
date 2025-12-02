package com.nhnacademy._vidiacouponservice.domain.dto.request;

public record CouponUseRequest(
        Long userId,
        Long orderId
) {}
