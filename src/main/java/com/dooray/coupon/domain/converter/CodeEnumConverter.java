package com.dooray.coupon.domain.converter;


import com.dooray.coupon.domain.common.CodeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.function.Function;

@Converter
public abstract class CodeEnumConverter<E extends Enum<E> & CodeEnum> implements AttributeConverter<E, Integer> {

    private final Function<Integer, E> toEnum;

    protected CodeEnumConverter(Function<Integer, E> toEnum) {
        this.toEnum = toEnum;
    }

    @Override
    public Integer convertToDatabaseColumn(E enumValue) {
        return enumValue == null ? null : enumValue.getCode();
    }

    @Override
    public E convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : toEnum.apply(dbData);
    }


}
