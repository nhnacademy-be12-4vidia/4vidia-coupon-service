package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 특정 정책에 속한 쿠폰들
    List<Coupon> findAllByCouponPolicy(CouponPolicy policy);

    // 정책 ID 기반 쿠폰 조회
    List<Coupon> findAllByCouponPolicy_PolicyId(Long policyId);

    List<Coupon> findTop5000ByStatusAndExpireAtBefore(CouponStatus status, LocalDateTime now);
}
