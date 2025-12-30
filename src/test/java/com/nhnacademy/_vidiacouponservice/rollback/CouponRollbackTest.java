package com.nhnacademy._vidiacouponservice.rollback;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.*;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CouponRollbackTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Test
    @DisplayName("주문 취소 시 쿠폰 상태 롤백 성공")
    void rollbackCouponsByOrderId() {
        // given
        Long orderId = 99L;

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyName("롤백 정책");
        policy.setPolicyType(PolicyType.EVENT);
        policy.setDiscountType(DiscountType.PRICE);
        policy.setDiscountValue(1000);
        policy.setDiscountTargetType(DiscountTargetType.ALL);
        policy.setValidityType(ValidityType.RELATIVE);
        policy.setValidDays(7);
        policy.setIsActivation(true);

        couponPolicyRepository.save(policy);

        Coupon coupon = new Coupon();
        coupon.setCouponPolicy(policy);
        coupon.setIssuedAt(LocalDateTime.now());
        coupon.setExpireAt(LocalDateTime.now().plusDays(1));
        coupon.setStatus(CouponStatus.USED);
        coupon.setUserOrderId(orderId);

        couponRepository.save(coupon);

        // when
        int updated = couponRepository.rollbackCouponsByOrderId(orderId, CouponStatus.USED, CouponStatus.UNUSED);

        // then
        assertThat(updated).isEqualTo(1);
    }
}
