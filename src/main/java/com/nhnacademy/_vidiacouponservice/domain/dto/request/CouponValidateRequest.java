package com.nhnacademy._vidiacouponservice.domain.dto.request;

import java.util.List;

public record CouponValidateRequest(
        Long couponId,
        int amount,
        List<Long> bookIds,
        List<Long> categoryIds
) {}
