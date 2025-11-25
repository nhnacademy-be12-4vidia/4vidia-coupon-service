package com.dooray.coupon.domain;

import com.dooray.coupon.domain.common.CouponStatus;
import com.dooray.coupon.domain.converter.CouponStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    private CouponPolicy couponPolicy;

    @Column(nullable = false)
    private LocalDateTime issuedAt;
    @Column(nullable = false)
    private LocalDateTime expireAt;
    private LocalDateTime usedAt;

    @Convert(converter = CouponStatusConverter.class)
    @Column(nullable = false)
    private CouponStatus status;


}
