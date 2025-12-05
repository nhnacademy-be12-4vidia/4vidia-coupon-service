package com.nhnacademy._vidiacouponservice.domain.dto.response;


import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;

import java.time.LocalDateTime;

public record MyCouponResponse(
        Long couponId,
        String policyName,
        String discountType,
        Integer discountValue,
        Integer maxDiscountAmount,
        String targetType,
        Long categoryId,
        Long bookId,
        LocalDateTime issuedAt,
        LocalDateTime expireAt,
        CouponStatus status,
        Long usedOrderId
) {}
