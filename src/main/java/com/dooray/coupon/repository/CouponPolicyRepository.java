package com.dooray.coupon.repository;

import com.dooray.coupon.domain.CouponPolicy;
import com.dooray.coupon.domain.common.PolicyType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CouponPolicyRepository extends CrudRepository<CouponPolicy, Long> {
    Optional<CouponPolicy> findByPolicyType(PolicyType policyType);
}
