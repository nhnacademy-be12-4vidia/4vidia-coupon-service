package com.nhnacademy._vidiacouponservice.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class RedisKeysTest {

    @Test
    @DisplayName("정책 ID로 재고 관리용 Redis 키를 생성")
    void stockKey_shouldBeGeneratedCorrectly() {
        assertThat(RedisKeys.stockKey(1L))
                .isEqualTo("coupon:policy:1:stock");
    }

    @Test
    @DisplayName("정책 ID와 유저 ID로 중복 발급 방지용 Redis 키를 생성")
    void dupKey_shouldBeGeneratedCorrectly() {
        assertThat(RedisKeys.dupKey(1L, 2L))
                .isEqualTo("coupon:dup:1:2");
    }

    @Test
    @DisplayName("웰컴 쿠폰 전용 중복 발급 방지 Redis 키를 생성")
    void welcomeDupKey_shouldBeGeneratedCorrectly() {
        assertThat(RedisKeys.welcomeDupKey(3L))
                .isEqualTo("coupon:welcome:3");
    }
}
