package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpireCalculator {

    public static LocalDateTime calcExpiry(CouponPolicy policy, LocalDateTime issuedAt) {

        // RELATIVE
        if (policy.getValidityType() == ValidityType.RELATIVE) {

            Integer days = policy.getValidDays();
            if (days == null || days <= 0) {
                throw new IllegalArgumentException("RELATIVE 정책은 validDays가 필수입니다.");
            }

            return issuedAt.plusDays(days);
        }

        // ABSOLUTE
        if (policy.getEndDate() == null) {
            throw new IllegalArgumentException("ABSOLUTE 정책은 endDate가 필수입니다.");
        }

        return policy.getEndDate().atTime(23, 59, 59);
    }
}
