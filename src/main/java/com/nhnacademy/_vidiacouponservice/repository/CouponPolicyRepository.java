package com.nhnacademy._vidiacouponservice.repository;


import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CouponPolicyRepository extends CrudRepository<CouponPolicy, Long> {
    Optional<CouponPolicy> findByPolicyType(PolicyType policyType);
}
