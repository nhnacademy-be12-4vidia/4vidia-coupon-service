package com.dooray.coupon.domain.converter;

import com.dooray.coupon.domain.common.CouponStatus;

public class CouponStatusConverter extends CodeEnumConverter<CouponStatus> {
    public CouponStatusConverter() {
        super(CouponStatus::findByCode);
    }
}
