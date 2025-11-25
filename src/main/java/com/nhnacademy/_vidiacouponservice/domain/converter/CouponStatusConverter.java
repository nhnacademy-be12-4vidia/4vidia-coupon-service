package com.nhnacademy._vidiacouponservice.domain.converter;


import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;

public class CouponStatusConverter extends CodeEnumConverter<CouponStatus> {
    public CouponStatusConverter() {
        super(CouponStatus::findByCode);
    }
}
