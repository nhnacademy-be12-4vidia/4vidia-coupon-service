package com.nhnacademy._vidiacouponservice.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DiscountType implements CodeEnum {
    PRICE(0),
    RATE(1);

    private final int code;

    @Override
    public int getCode() {
        return code;
    }

    public static DiscountType findByCode(int code) {
        return Arrays.stream(DiscountType.values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid discount type"));
    }
}
