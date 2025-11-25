package com.dooray.coupon.domain;

import com.dooray.coupon.domain.common.DiscountTargetType;
import com.dooray.coupon.domain.common.DiscountType;
import com.dooray.coupon.domain.common.PolicyType;
import com.dooray.coupon.domain.common.ValidityType;
import com.dooray.coupon.domain.converter.*;
import com.dooray.coupon.domain.dto.CouponPolicyUpdaterequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_policy_id", nullable = false)
    private Long policyId;

    @Column(name = "coupon_policy_name", nullable = false)
    private String policyName;

    @Convert(converter = PolicyTypeConverter.class)
    @Column(name = "policy_type", nullable = false)
    private PolicyType policyType;

    @Convert(converter = DiscountTypeConverter.class)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false)
    private Integer discountValue;

    @Convert(converter = DiscountTargetTypeConverter.class)
    @Column(name = "discount_target_type",  nullable = false)
    private DiscountTargetType discountTargetType;

    private Long categoryId;
    private Long bookId;

    @Convert(converter = ValidityTypeConverter.class)
    @Column(name = "validity_type", nullable = false)
    private ValidityType validityType;

    @Column(name = "valid_days")
    private Integer valid_days;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    /**
    null이면 무제한
     */
    @Column(name = "limited_quantity")
    private Integer limitedQuantity;

    /**
     * 기본은 0
     */
    @Column(name = "issued_quantity",  nullable = false)
    private Integer issuedQuantity;
    /**
     * 기본은 0
     */
    @Column(name = "min_order_amount",  nullable = false)
    private Integer minOrderAmount;
    /**
     * 기본은 0
     */
    @Column(name = "max_discount_amount",  nullable = false)
    private Integer maxDiscountAmount;

    /**
     * 기본은 0
     */
    @Column(name = "is_activation",  nullable = false)
    private Boolean isActivation;


    //service용
    public void update(CouponPolicyUpdaterequest dto){
        this.policyName = dto.policyName();
        this.policyType = dto.policyType();
        this.discountType = dto.discountType();
        this.discountValue = dto.discountValue();
        this.discountTargetType = dto.discountTargetType();
        this.categoryId = dto.categoryId();
        this.bookId = dto.bookId();
        this.validityType = dto.validityType();
        this.valid_days = dto.validDays();
        this.startDate = dto.startDate();
        this.endDate = dto.endDate();
        this.limitedQuantity = dto.limitedQuantity();
        this.minOrderAmount = dto.minOrderAmount();
        this.maxDiscountAmount = dto.maxDiscountAmount();
        this.isActivation = dto.isActivation();
    }


}
