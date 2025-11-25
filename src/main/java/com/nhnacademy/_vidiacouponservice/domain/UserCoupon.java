package com.nhnacademy._vidiacouponservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_coupon")
public class UserCoupon {
    @EmbeddedId
    private UserCouponId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("couponId")
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    public UserCoupon(Long userId, Coupon coupon) {
        this.coupon =  coupon;
        this.id = new UserCouponId(coupon.getCouponId(), userId);
    }
}
