package com.nhnacademy._vidiacouponservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ---- 1xxx 인증/인가 ----
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // ---- 2xxx 정책 ----
    POLICY_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠폰 정책을 찾을 수 없습니다."),
    POLICY_INACTIVE(HttpStatus.BAD_REQUEST, "비활성화된 정책입니다."),
    POLICY_OUT_OF_STOCK(HttpStatus.CONFLICT, "쿠폰 재고가 모두 소진되었습니다."),
    POLICY_STOCK_MISSING(HttpStatus.BAD_REQUEST, "정책 재고 정보가 없습니다."),
    POLICY_INVALID(HttpStatus.BAD_REQUEST, "잘못된 정책 요청입니다."),

    // ---- 3xxx 쿠폰 ----
    COUPON_ALREADY_USED(HttpStatus.CONFLICT, "이미 사용된 쿠폰입니다."),
    COUPON_EXPIRED(HttpStatus.GONE, "만료된 쿠폰입니다."),

    // ---- 9xxx 시스템 ----
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
