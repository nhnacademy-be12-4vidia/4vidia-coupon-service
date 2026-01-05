package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import com.nhnacademy._vidiacouponservice.setting.CouponPolicyTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BirthdayCouponServiceTest {

    @InjectMocks
    BirthdayCouponService service;

    @Mock
    CouponPolicyRepository couponPolicyRepository;

    @Mock
    CouponEventIssueService couponEventIssueService;

    @Test
    @DisplayName("BIRTHDAY 정책이 활성화되어 있으면 생일 쿠폰이 자동 발급된다")
    void giveBirthdayCoupon_success() {
        // given
        CouponPolicy policy =
                CouponPolicyTestFactory.birthdayPolicy();
        policy.setPolicyId(2L);

        when(couponPolicyRepository
                .findByPolicyTypeAndIsActivationTrue(PolicyType.BIRTHDAY))
                .thenReturn(Optional.of(policy));

        // when
        service.giveBirthdayCoupon(20L);

        // then
        verify(couponEventIssueService)
                .issueEventCoupon(20L, 2L);
    }

    @Test
    @DisplayName("BIRTHDAY 정책이 없으면 아무 일도 일어나지 않는다")
    void giveBirthdayCoupon_policyNotFound() {
        // given
        when(couponPolicyRepository
                .findByPolicyTypeAndIsActivationTrue(PolicyType.BIRTHDAY))
                .thenReturn(Optional.empty());

        // when
        service.giveBirthdayCoupon(20L);

        // then
        verify(couponEventIssueService, never())
                .issueEventCoupon(anyLong(), anyLong());
    }
}
