package com.nhnacademy._vidiacouponservice.domain.dto.response;

import java.time.LocalDateTime;

public record OrderPageCouponResponse(
        Long couponId,
        String policyName,
        String discountType,       // PRICE / RATE 구분
        Integer discountValue,    // 3000원짜리 쿠폰이면 3000원 표시, 퍼센트면 총금액이 10000원이면 10퍼할인쿠폰이면 1000원
        Integer discountPrice,     // 받은 amount-discountAmount
        LocalDateTime expireAt,
        boolean available,
        String reason
) {}
