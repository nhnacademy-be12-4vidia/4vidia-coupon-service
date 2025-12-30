package com.nhnacademy._vidiacouponservice.domain.converter;

import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PolicyTypeConverterTest {

    private final PolicyTypeConverter converter = new PolicyTypeConverter();

    @Test
    @DisplayName("Enum → DB 코드 변환 성공")
    void convertToDatabaseColumn() {
        PolicyType type = PolicyType.EVENT;

        Integer result = converter.convertToDatabaseColumn(type);

        assertThat(result).isEqualTo(type.getCode());
    }

    @Test
    @DisplayName("DB 코드 → Enum 변환 성공")
    void convertToEntityAttribute() {
        Integer dbCode = PolicyType.BIRTHDAY.getCode();

        PolicyType result = converter.convertToEntityAttribute(dbCode);

        assertThat(result).isEqualTo(PolicyType.BIRTHDAY);
    }

    @Test
    @DisplayName("null 입력 시 null 반환")
    void convertNull() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 코드 입력 시 예외 발생")
    void convertInvalidCode() {
        assertThatThrownBy(() -> converter.convertToEntityAttribute(123))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
