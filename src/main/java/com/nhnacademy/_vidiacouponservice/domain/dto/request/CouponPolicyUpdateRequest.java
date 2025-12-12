package com.nhnacademy._vidiacouponservice.domain.dto.request;



import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;

public record CouponPolicyUpdateRequest(
        String policyName,
        Integer discountValue,
        DiscountTargetType discountTargetType,
        Integer minOrderAmount,
        Integer maxDiscountAmount
) {}
