package com.nhnacademy._vidiacouponservice.domain;


import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;
import com.nhnacademy._vidiacouponservice.domain.converter.DiscountTargetTypeConverter;
import com.nhnacademy._vidiacouponservice.domain.converter.DiscountTypeConverter;
import com.nhnacademy._vidiacouponservice.domain.converter.PolicyTypeConverter;
import com.nhnacademy._vidiacouponservice.domain.converter.ValidityTypeConverter;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyCreateRequest;
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
@Table(name = "coupon_policy")
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

    @Column(name = "category_kdc_id")
    private String categoryKdcId;

    @Column(name = "book_id")
    private Long bookId;

    @Convert(converter = ValidityTypeConverter.class)
    @Column(name = "validity_type", nullable = false)
    private ValidityType validityType;

    @Column(name = "valid_days")
    private Integer validDays;

    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
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
     * 기본은 1
     */
    @Column(name = "is_activation",  nullable = false)
    private Boolean isActivation;

    /**
     * DB issued_quantity증가용
     */
    public void increaseIssuedQuantity() {
        this.issuedQuantity = this.issuedQuantity + 1;
    }



    //service용
    public static CouponPolicy create(CouponPolicyCreateRequest dto) {
        CouponPolicy p = new CouponPolicy();

        p.policyName = dto.policyName();
        p.policyType = dto.policyType();
        p.discountType = dto.discountType();
        p.discountValue = dto.discountValue();
        p.discountTargetType = dto.discountTargetType();
        p.categoryKdcId = dto.categoryKdcId();
        p.bookId = dto.bookId();
        p.validityType = dto.validityType();
        p.validDays = dto.validDays();
        p.startDate = dto.startDate();
        p.endDate = dto.endDate();
        p.limitedQuantity = dto.limitedQuantity();
        p.minOrderAmount = dto.minOrderAmount();
        p.maxDiscountAmount = dto.maxDiscountAmount();
        p.issuedQuantity = 0;
        p.isActivation = dto.isActivation() != null ? dto.isActivation() : true;

        return p;
    }


}
