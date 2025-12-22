package com.nhnacademy._vidiacouponservice.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum CouponStatus implements CodeEnum {
    UNUSED(0),
    USED(1),
    EXPIRED(2);

    private final int code;

    @Override
    public int getCode() {
        return code;
    }

    public static CouponStatus findByCode(int code) {
        return Stream.of(CouponStatus.values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid coupon status"));
    }

}
