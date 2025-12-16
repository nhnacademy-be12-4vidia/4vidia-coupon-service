package com.nhnacademy._vidiacouponservice.domain.dto.request;

import com.nhnacademy._vidiacouponservice.domain.dto.response.OrderPageCouponResponse;

import java.util.List;

public record OrderCouponResult(
        List<OrderPageCouponResponse> possibleCoupons,
        List<OrderPageCouponResponse> impossibleCoupons
) {}
