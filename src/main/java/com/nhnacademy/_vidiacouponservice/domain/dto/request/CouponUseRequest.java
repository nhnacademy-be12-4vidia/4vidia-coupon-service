package com.nhnacademy._vidiacouponservice.domain.dto.request;

import java.util.List;

public record CouponUseRequest(
        List<Long> couponIds,
        Long orderId
) {}
