package com.dooray.coupon.domain.converter;

import com.dooray.coupon.domain.common.PolicyType;

public class PolicyTypeConverter extends CodeEnumConverter<PolicyType> {
    public PolicyTypeConverter() {
        super(PolicyType::findByCode);
    }
}
