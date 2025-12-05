package com.nhnacademy._vidiacouponservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
public class UserCouponId implements Serializable {
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "user_id")
    private Long userId;

}
