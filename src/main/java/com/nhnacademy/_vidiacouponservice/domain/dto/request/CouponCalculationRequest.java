package com.nhnacademy._vidiacouponservice.domain.dto.request;

import java.util.List;

public record CouponCalculationRequest(
        Long couponId,      // 선택된 쿠폰 ID 목록
        List<ItemInfo> items       // 주문 도서 정보 (원본)
) {
    public record ItemInfo(
            Long bookId,
            String categoryKdcId,
            int price,             // 단가
            int quantity           // 수량
    ) {}
}
