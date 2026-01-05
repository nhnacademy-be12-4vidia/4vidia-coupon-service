package com.nhnacademy._vidiacouponservice.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RedisConfigTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("RedisTemplate 빈이 정상적으로 생성")
    void redisTemplateBean_shouldBeCreated() {
        assertThat(redisTemplate).isNotNull();
    }

    @Test
    @DisplayName("RedisTemplate은 String 기반 직렬화기를 사용")
    void redisTemplate_shouldHaveStringSerializers() {
        assertThat(redisTemplate.getKeySerializer()).isNotNull();
        assertThat(redisTemplate.getValueSerializer()).isNotNull();
    }
}
