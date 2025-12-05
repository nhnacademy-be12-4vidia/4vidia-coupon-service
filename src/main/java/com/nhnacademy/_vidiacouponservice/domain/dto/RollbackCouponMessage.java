package com.nhnacademy._vidiacouponservice.domain.dto;

import java.util.List;

public record RollbackCouponMessage(
        Long orderId,
        List<Long> couponIds
) {}
