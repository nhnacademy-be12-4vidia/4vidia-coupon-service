package com.nhnacademy._vidiacouponservice.domain.converter;

import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DiscountTypeConverterTest {

    private final DiscountTypeConverter converter = new DiscountTypeConverter();

    @Test
    @DisplayName("Enum → DB 코드 변환 성공")
    void convertToDatabaseColumn() {
        DiscountType type = DiscountType.PRICE;

        Integer result = converter.convertToDatabaseColumn(type);

        assertThat(result).isEqualTo(type.getCode());
    }

    @Test
    @DisplayName("DB 코드 → Enum 변환 성공")
    void convertToEntityAttribute() {
        Integer dbCode = DiscountType.RATE.getCode();

        DiscountType result = converter.convertToEntityAttribute(dbCode);

        assertThat(result).isEqualTo(DiscountType.RATE);
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
        assertThatThrownBy(() -> converter.convertToEntityAttribute(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
