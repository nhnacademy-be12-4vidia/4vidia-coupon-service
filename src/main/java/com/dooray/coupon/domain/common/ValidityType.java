package com.dooray.coupon.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ValidityType implements CodeEnum {
    RELATIVE(0),
    ABSOLUTE(1);

    private final int code;

    @Override
    public int getCode() {
        return code;
    }

    public static ValidityType findByCode(int code) {
        return Arrays.stream(ValidityType.values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid validity type"));
    }
}
