package com.dooray.coupon.domain.converter;

import com.dooray.coupon.domain.common.ValidityType;

public class ValidityTypeConverter extends CodeEnumConverter<ValidityType> {
    public ValidityTypeConverter() {
        super(ValidityType::findByCode);
    }
}
