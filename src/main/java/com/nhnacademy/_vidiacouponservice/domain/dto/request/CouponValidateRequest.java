package com.nhnacademy._vidiacouponservice.domain.dto.request;

import java.util.List;

public record CouponValidateRequest(
        int amount,
        List<Long> bookIds,
        List<String> categoryKdcIds
) {}
