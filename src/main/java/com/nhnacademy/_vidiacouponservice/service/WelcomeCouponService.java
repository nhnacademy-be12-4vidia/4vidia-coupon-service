package com.nhnacademy._vidiacouponservice.service;


import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WelcomeCouponService {
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponEventIssueService couponService;

    public void giveWelcomeCoupon(Long userId){
        CouponPolicy policy = couponPolicyRepository.findByPolicyType(PolicyType.WELCOME)
                .orElseThrow(() -> new IllegalArgumentException("WELCOME 정책 없음"));

        couponService.issueEventCoupon(userId, policy.getPolicyId());
    }

}
