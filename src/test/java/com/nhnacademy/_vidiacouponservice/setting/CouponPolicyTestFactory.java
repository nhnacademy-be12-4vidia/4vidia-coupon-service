package com.nhnacademy._vidiacouponservice.setting;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.*;

import java.time.LocalDate;

public class CouponPolicyTestFactory {

    /**
     * 기본 활성화 이벤트 쿠폰 (ALL 대상)
     */
    public static CouponPolicy activeEventAllPolicy() {
        CouponPolicy policy = basePolicy();

        policy.setPolicyName("ACTIVE_EVENT_ALL");
        policy.setPolicyType(PolicyType.EVENT);
        policy.setDiscountTargetType(DiscountTargetType.ALL);

        return policy;
    }

    /**
     * 비활성화 쿠폰 정책
     */
    public static CouponPolicy inactivePolicy() {
        CouponPolicy policy = activeEventAllPolicy();
        policy.setPolicyName("INACTIVE_POLICY");
        policy.setIsActivation(false);
        return policy;
    }

    /**
     * 생일 쿠폰 정책
     */
    public static CouponPolicy birthdayPolicy() {
        CouponPolicy policy = basePolicy();

        policy.setPolicyName("BIRTHDAY_POLICY");
        policy.setPolicyType(PolicyType.BIRTHDAY);
        policy.setDiscountTargetType(DiscountTargetType.ALL);

        return policy;
    }

    /**
     * 카테고리 쿠폰 정책
     */
    public static CouponPolicy categoryPolicy(String categoryKdcId) {
        CouponPolicy policy = basePolicy();

        policy.setPolicyName("CATEGORY_POLICY");
        policy.setDiscountTargetType(DiscountTargetType.CATEGORY);
        policy.setCategoryKdcId(categoryKdcId);

        return policy;
    }

    /**
     * 도서 쿠폰 정책
     */
    public static CouponPolicy bookPolicy(Long bookId) {
        CouponPolicy policy = basePolicy();

        policy.setPolicyName("BOOK_POLICY");
        policy.setDiscountTargetType(DiscountTargetType.BOOK);
        policy.setBookId(bookId);

        return policy;
    }

    /**
     * 기간 고정(ABSOLUTE) 쿠폰
     */
    public static CouponPolicy absolutePeriodPolicy() {
        CouponPolicy policy = basePolicy();

        policy.setPolicyName("ABSOLUTE_POLICY");
        policy.setValidityType(ValidityType.ABSOLUTE);
        policy.setStartDate(LocalDate.now());
        policy.setEndDate(LocalDate.now().plusDays(7));

        return policy;
    }

    /**
     * ===== 공통 베이스 =====
     * DB NOT NULL 전부 채워주는 핵심
     */
    private static CouponPolicy basePolicy() {
        CouponPolicy policy = new CouponPolicy();

        policy.setPolicyName("BASE_POLICY");
        policy.setPolicyType(PolicyType.EVENT);
        policy.setIsActivation(true);

        policy.setDiscountType(DiscountType.PRICE);
        policy.setDiscountValue(1000);
        policy.setDiscountTargetType(DiscountTargetType.ALL);

        policy.setMinOrderAmount(0);
        policy.setMaxDiscountAmount(10_000);

        policy.setIssuedQuantity(0);
        policy.setLimitedQuantity(100);

        policy.setValidityType(ValidityType.RELATIVE);
        policy.setValidDays(30);

        return policy;
    }
}
