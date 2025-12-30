package com.nhnacademy._vidiacouponservice.domain.dto.response;

public record MyCouponPageResponse(
        PageResponse<MyCouponResponse> page,
        long totalCount,        // UNUSED 개수 (상단 “보유 쿠폰”)
        long expireSoonCount    // UNUSED 중 7일 이내 만료
) {}
