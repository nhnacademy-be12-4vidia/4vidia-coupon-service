package com.dooray.coupon.domain.converter;

import com.dooray.coupon.domain.common.DiscountTargetType;

public class DiscountTargetTypeConverter extends CodeEnumConverter<DiscountTargetType> {
    public DiscountTargetTypeConverter() {
        super(DiscountTargetType::findByCode);
    }
}
