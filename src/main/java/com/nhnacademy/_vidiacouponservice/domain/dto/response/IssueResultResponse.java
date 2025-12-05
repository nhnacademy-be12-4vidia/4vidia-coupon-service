package com.nhnacademy._vidiacouponservice.domain.dto.response;

public record IssueResultResponse(
        boolean success,
        String message,
        Long couponId
) {
    public static IssueResultResponse ok(Long couponId) {
        return new IssueResultResponse(true, "쿠폰 발급 성공", couponId);
    }

    public static IssueResultResponse fail(String msg) {
        return new IssueResultResponse(false, msg, null);
    }
}
