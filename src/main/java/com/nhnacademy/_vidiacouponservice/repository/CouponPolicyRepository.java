package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

    // 활성 정책만 조회
    List<CouponPolicy> findAllByIsActivationTrue();

    // 특정 타입의 정책 조회 (EX: WELCOME, BIRTHDAY)
    List<CouponPolicy> findAllByPolicyType(PolicyType type);

    Optional<CouponPolicy> findByPolicyType(PolicyType type);
}
