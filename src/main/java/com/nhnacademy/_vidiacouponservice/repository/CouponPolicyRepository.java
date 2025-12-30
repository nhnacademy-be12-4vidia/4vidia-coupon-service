package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

    // 활성 정책만 조회
    List<CouponPolicy> findAllByIsActivationTrue();

    Optional<CouponPolicy> findByPolicyType(PolicyType type);

    @Query("""
    select p from CouponPolicy p
    where (:keyword is null or p.policyName like %:keyword%)
    and (
        :status is null
        or (:status = 'ACTIVE' and p.isActivation = true)
        or (:status = 'INACTIVE' and p.isActivation = false)
    )
    and (:targetType is null or p.discountTargetType = :targetType)
    """)
    Page<CouponPolicy> search(
            String keyword,
            String status,
            DiscountTargetType targetType,
            Pageable pageable
    );

    Optional<CouponPolicy> findByPolicyTypeAndIsActivationTrue(PolicyType policyType);


}
