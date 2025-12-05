package com.nhnacademy._vidiacouponservice.domain.dto.response;

import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;

import java.time.LocalDateTime;

public record CouponDetailResponse(
        Long couponId,
        Long policyId,
        String policyName,
        Integer discountValue,
        String discountType,
        LocalDateTime issuedAt,
        LocalDateTime expireAt,
        LocalDateTime usedAt,
        CouponStatus status,
        Long usedOrderId
) {}
