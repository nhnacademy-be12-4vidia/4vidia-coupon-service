package com.nhnacademy._vidiacouponservice.domain.converter;


import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;

public class ValidityTypeConverter extends CodeEnumConverter<ValidityType> {
    public ValidityTypeConverter() {
        super(ValidityType::findByCode);
    }
}
