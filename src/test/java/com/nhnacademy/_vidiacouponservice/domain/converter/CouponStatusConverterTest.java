package com.nhnacademy._vidiacouponservice.domain.converter;

import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CouponStatusConverterTest {

    private final CouponStatusConverter converter = new CouponStatusConverter();

    @Test
    @DisplayName("Enum → DB 코드 변환 성공")
    void convertToDatabaseColumn() {
        CouponStatus status = CouponStatus.USED;

        Integer result = converter.convertToDatabaseColumn(status);

        assertThat(result).isEqualTo(status.getCode());
    }

    @Test
    @DisplayName("DB 코드 → Enum 변환 성공")
    void convertToEntityAttribute() {
        Integer dbCode = CouponStatus.EXPIRED.getCode();

        CouponStatus result = converter.convertToEntityAttribute(dbCode);

        assertThat(result).isEqualTo(CouponStatus.EXPIRED);
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
        assertThatThrownBy(() -> converter.convertToEntityAttribute(999))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
