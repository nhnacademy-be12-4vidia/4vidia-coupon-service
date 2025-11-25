package com.nhnacademy._vidiacouponservice.domain.converter;


import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;

public class DiscountTargetTypeConverter extends CodeEnumConverter<DiscountTargetType> {
    public DiscountTargetTypeConverter() {
        super(DiscountTargetType::findByCode);
    }
}
