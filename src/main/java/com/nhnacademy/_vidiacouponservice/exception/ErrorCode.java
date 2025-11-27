package com.nhnacademy._vidiacouponservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 1xxx 인증/인가
    UNAUTHORIZED(1001, "인증이 필요합니다."),
    FORBIDDEN(1002, "권한이 없습니다."),

    // 2xxx 정책
    POLICY_NOT_FOUND(2001, "쿠폰 정책을 찾을 수 없습니다."),
    POLICY_INACTIVE(2002, "비활성화된 정책입니다."),
    POLICY_OUT_OF_STOCK(2003, "쿠폰 재고가 모두 소진되었습니다."),
    POLICY_INVALID(2004, "잘못된 정책 요청입니다."),

    // 3xxx 쿠폰
    COUPON_ALREADY_USED(3001, "이미 사용된 쿠폰입니다."),
    COUPON_EXPIRED(3002, "만료된 쿠폰입니다."),

    // 9xxx 시스템
    INTERNAL_SERVER_ERROR(9999, "서버 내부 오류가 발생했습니다.");

    private final int code;
    private final String message;
}
