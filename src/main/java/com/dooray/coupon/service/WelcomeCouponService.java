package com.dooray.coupon.service;

import com.dooray.coupon.domain.CouponPolicy;
import com.dooray.coupon.domain.common.PolicyType;
import com.dooray.coupon.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WelcomeCouponService {
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponService couponService;

    public void giveWelcomeCoupon(Long userId){
        CouponPolicy policy = couponPolicyRepository.findByPolicyType(PolicyType.WELCOME)
                .orElseThrow(() -> new IllegalArgumentException("WELLCOME 정책 없음"));

        couponService.issue2(userId, policy.getPolicyId());
    }

}
