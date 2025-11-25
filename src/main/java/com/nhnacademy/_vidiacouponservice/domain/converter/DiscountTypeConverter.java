package com.nhnacademy._vidiacouponservice.domain.converter;


import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;

public class DiscountTypeConverter extends CodeEnumConverter<DiscountType> {
    public DiscountTypeConverter() {
        super(DiscountType::findByCode);
    }
}
