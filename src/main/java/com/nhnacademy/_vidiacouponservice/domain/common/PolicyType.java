package com.nhnacademy._vidiacouponservice.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PolicyType implements CodeEnum {
    WELCOME(0),
    BIRTHDAY(1),
    EVENT(2);

    private final int code;

    @Override
    public int getCode() {
        return code;
    }

    public static PolicyType findByCode(int code) {
        return Arrays.stream(PolicyType.values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid policy type"));
    }
}

