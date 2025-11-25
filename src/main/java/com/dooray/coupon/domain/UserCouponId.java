package com.dooray.coupon.domain;

import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
public class UserCouponId implements Serializable {
    private Long couponId;
    private Long userId;

}
