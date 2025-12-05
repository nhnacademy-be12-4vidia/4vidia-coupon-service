package com.nhnacademy._vidiacouponservice.domain.dto.response;

import java.util.List;

public record CouponListResponse(
        List<CouponDetailResponse> coupons
) {}
