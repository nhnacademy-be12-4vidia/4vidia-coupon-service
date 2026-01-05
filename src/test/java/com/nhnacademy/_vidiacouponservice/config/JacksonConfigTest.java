package com.nhnacademy._vidiacouponservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class JacksonConfigTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Jackson ObjectMapper 빈이 정상적으로 생성")
    void objectMapperBean_shouldBeCreated() {
        assertThat(objectMapper).isNotNull();
    }

    @Test
    @DisplayName("ObjectMapper는 LocalDateTime 타입을 JSON으로 직렬화")
    void objectMapper_shouldSerializeLocalDateTime() throws Exception {
        String json = objectMapper.writeValueAsString(LocalDateTime.now());
        assertThat(json).isNotBlank();
    }
}
