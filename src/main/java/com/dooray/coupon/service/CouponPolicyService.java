package com.dooray.coupon.service;


import com.dooray.coupon.domain.CouponPolicy;
import com.dooray.coupon.domain.dto.CouponPolicyUpdaterequest;
import com.dooray.coupon.repository.CouponPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;

    public CouponPolicy createPolicy(CouponPolicy policy) {
        policy.setIssuedQuantity(0);
        policy.setIsActivation(true);

        return couponPolicyRepository.save(policy);
    }

    public CouponPolicy updatePolicy(Long policyId, CouponPolicyUpdaterequest dto) {
        CouponPolicy policy = findPolicy(policyId);
        policy.update(dto);
        return policy;
    }

    @Transactional(readOnly = true)
    public Iterable<CouponPolicy> listPolicies() {
        return couponPolicyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CouponPolicy findPolicy(Long policyId) {
        return couponPolicyRepository.findById(policyId).orElseThrow(() -> new IllegalArgumentException("정책없음"));
    }

    public void deactivatePolicy(Long policyId) {
        CouponPolicy policy = findPolicy(policyId);
        policy.setIsActivation(false);
    }

    public void changActivePolicy(Long policyId, boolean isActivation) {
        CouponPolicy policy = findPolicy(policyId);
        policy.setIsActivation(isActivation);
    }

}
