package com.nhnacademy._vidiacouponservice.domain.dto.response;

import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;

import java.time.LocalDateTime;

public record CouponPolicyResponse(
        Long policyId,
        String policyName,
        PolicyType policyType,
        DiscountType discountType,
        Integer discountValue,
        DiscountTargetType discountTargetType,
        String categoryKdcId,
        Long bookId,
        ValidityType validityType,
        Integer validDays,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer limitedQuantity,
        Integer issuedQuantity,
        Integer minOrderAmount,
        Integer maxDiscountAmount,
        Boolean isActivation
) {}
