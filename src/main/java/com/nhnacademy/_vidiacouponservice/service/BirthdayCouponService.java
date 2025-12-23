package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotFoundException;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BirthdayCouponService {
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponEventIssueService couponEventIssueService;

    public Optional<CouponPolicy> findActivePolicy(PolicyType policyType) {
        return couponPolicyRepository
                .findByPolicyTypeAndIsActivationTrue(policyType);
    }

    public void giveBirthdayCoupon(Long userId) {

        CouponPolicy policy = couponPolicyRepository
                .findByPolicyTypeAndIsActivationTrue(PolicyType.BIRTHDAY)
                .orElse(null);

        if (policy == null) {
            // 정책 없으면 그냥 스킵 (graceful fallback)
            log.warn("생일정책 없음");
            return;
        }

        couponEventIssueService.issueEventCoupon(
                userId,
                policy.getPolicyId()
        );
    }

}
