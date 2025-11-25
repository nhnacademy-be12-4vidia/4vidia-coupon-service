package com.dooray.coupon.domain.dto;

import com.dooray.coupon.domain.common.DiscountTargetType;
import com.dooray.coupon.domain.common.DiscountType;
import com.dooray.coupon.domain.common.PolicyType;
import com.dooray.coupon.domain.common.ValidityType;

import java.time.LocalDateTime;

public record CouponPolicyUpdaterequest(
        String policyName,
        PolicyType policyType,
        DiscountType discountType,
        DiscountTargetType discountTargetType,
        Integer discountValue,
        Long categoryId,
        Long bookId,
        ValidityType validityType,
        Integer validDays,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer limitedQuantity,
        Integer minOrderAmount,
        Integer maxDiscountAmount,
        Boolean isActivation
) {}
