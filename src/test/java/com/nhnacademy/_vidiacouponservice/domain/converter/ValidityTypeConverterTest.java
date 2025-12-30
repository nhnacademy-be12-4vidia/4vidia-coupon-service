package com.nhnacademy._vidiacouponservice.domain.converter;

import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ValidityTypeConverterTest {

    private final ValidityTypeConverter converter = new ValidityTypeConverter();

    @Test
    @DisplayName("Enum → DB 코드 변환 성공")
    void convertToDatabaseColumn() {
        ValidityType type = ValidityType.RELATIVE;

        Integer result = converter.convertToDatabaseColumn(type);

        assertThat(result).isEqualTo(type.getCode());
    }

    @Test
    @DisplayName("DB 코드 → Enum 변환 성공")
    void convertToEntityAttribute() {
        Integer dbCode = ValidityType.ABSOLUTE.getCode();

        ValidityType result = converter.convertToEntityAttribute(dbCode);

        assertThat(result).isEqualTo(ValidityType.ABSOLUTE);
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
        assertThatThrownBy(() -> converter.convertToEntityAttribute(0xDEAD))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
