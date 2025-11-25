package com.dooray.coupon.domain.converter;

import com.dooray.coupon.domain.common.DiscountType;

public class DiscountTypeConverter extends CodeEnumConverter<DiscountType> {
    public DiscountTypeConverter() {
        super(DiscountType::findByCode);
    }
}
