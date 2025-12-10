package com.nhnacademy._vidiacouponservice.domain;


import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.domain.converter.CouponStatusConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    private CouponPolicy couponPolicy;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;
    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expireAt;
    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Convert(converter = CouponStatusConverter.class)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(name = "user_order_id")
    private Long userOrderId;

    public static Coupon issue(CouponPolicy policy,
                               LocalDateTime issuedAt,
                               LocalDateTime expireAt) {
        Coupon c = new Coupon();
        c.couponPolicy = policy;
        c.issuedAt = issuedAt;
        c.expireAt = expireAt;
        c.status = CouponStatus.UNUSED;
        return c;
    }

}
