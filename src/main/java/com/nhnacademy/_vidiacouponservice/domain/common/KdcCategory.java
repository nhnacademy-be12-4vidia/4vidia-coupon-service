package com.nhnacademy._vidiacouponservice.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum KdcCategory {

    GENERAL("0", "총류"),
    PHILOSOPHY("1", "철학"),
    RELIGION("2", "종교"),
    SOCIAL_SCIENCE("3", "사회과학"),
    NATURAL_SCIENCE("4", "자연과학"),
    TECHNOLOGY("5", "기술과학"),
    ART("6", "예술"),
    LANGUAGE("7", "언어"),
    LITERATURE("8", "문학"),
    HISTORY("9", "역사");

    private final String code;   // 앞자리 숫자 1자리
    private final String desc;

    public static KdcCategory fromKdcId(String kdcId) {
        return Arrays.stream(values())
                .filter(v -> kdcId.startsWith(v.code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 KDC 코드입니다: " + kdcId));
    }
}
