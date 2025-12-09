package com.nhnacademy._vidiacouponservice.domain.dto.request;



import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;

public record CouponPolicyUpdateRequest(
        String policyName,
        DiscountType discountType,
        Integer discountValue,
        DiscountTargetType discountTargetType,
        Long categoryId,
        Long bookId,
        Integer minOrderAmount,
        Integer maxDiscountAmount
) {}
