package com.nhnacademy._vidiacouponservice.domain.dto.request;

import java.util.List;


public record CouponValidateRequest(
        List<CouponValidateItem> items
) {}
