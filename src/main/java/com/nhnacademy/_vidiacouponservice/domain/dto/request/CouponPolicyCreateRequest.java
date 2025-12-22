package com.nhnacademy._vidiacouponservice.domain.dto.request;

import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CouponPolicyCreateRequest(
        String policyName,
        PolicyType policyType,
        DiscountType discountType,
        DiscountTargetType discountTargetType,
        Integer discountValue,
        String categoryKdcId,
        Long bookId,
        ValidityType validityType,
        Integer validDays,
        LocalDate startDate,
        LocalDate endDate,
        Integer limitedQuantity,
        Integer minOrderAmount,
        Integer maxDiscountAmount,
        Boolean isActivation
) {}
