package com.nhnacademy._vidiacouponservice.domain.dto.response;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountType;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;

import java.time.LocalDateTime;

public record CouponPolicyResponse(
        Long policyId,
        String policyName,
        DiscountType discountType,
        Integer discountValue,
        Integer maxDiscountAmount,
        DiscountTargetType discountTargetType,
        ValidityType validityType,
        Integer validDays,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean isActivation,
        String issueStatus
) {
    public static CouponPolicyResponse from(CouponPolicy p) {

        String issueStatus;

        if (p.getLimitedQuantity() == null) {
            issueStatus = "무제한"; // 무제한
        } else {
            int total = p.getLimitedQuantity();
            int issued = p.getIssuedQuantity(); // or Redis issued
            issueStatus = total + " / " + (total - issued);
        }

        return new CouponPolicyResponse(
                p.getPolicyId(),
                p.getPolicyName(),
                p.getDiscountType(),
                p.getDiscountValue(),
                p.getMaxDiscountAmount(),
                p.getDiscountTargetType(),
                p.getValidityType(),
                p.getValidDays(),
                p.getStartDate(),
                p.getEndDate(),
                p.getIsActivation(),
                issueStatus
        );
    }
}
