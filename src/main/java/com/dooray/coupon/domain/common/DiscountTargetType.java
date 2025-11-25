package com.dooray.coupon.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DiscountTargetType implements CodeEnum {
    ALL(0),
    CATEGORY(1),
    BOOK(2);

     private final int code;

     @Override
     public int getCode() {
         return code;
     }

     public static DiscountTargetType findByCode(int code){
         return Arrays.stream(DiscountTargetType.values())
                 .filter(v -> v.code == code)
                 .findFirst()
                 .orElseThrow(() -> new IllegalArgumentException("Invalid discountTarget type"));
     }
}
