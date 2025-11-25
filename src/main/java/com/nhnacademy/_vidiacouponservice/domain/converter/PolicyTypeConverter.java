package com.nhnacademy._vidiacouponservice.domain.converter;


import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;

public class PolicyTypeConverter extends CodeEnumConverter<PolicyType> {
    public PolicyTypeConverter() {
        super(PolicyType::findByCode);
    }
}
