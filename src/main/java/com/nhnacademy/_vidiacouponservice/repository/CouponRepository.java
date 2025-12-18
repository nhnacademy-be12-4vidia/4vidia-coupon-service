package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 동시성 해결
    @Modifying
    @Query("""
    UPDATE Coupon c
       SET c.status = :usedStatus,
           c.usedAt = :usedAt,
           c.userOrderId = :orderId
     WHERE c.couponId = :couponId
       AND c.status = :unusedStatus
       AND c.expireAt > :now
    """)
    int useCouponIfUnusedAndNotExpired(
            @Param("couponId") Long couponId,
            @Param("orderId") Long orderId,
            @Param("usedAt") LocalDateTime usedAt,
            @Param("now") LocalDateTime now,
            @Param("unusedStatus") CouponStatus unusedStatus,
            @Param("usedStatus") CouponStatus usedStatus
    );

    
    
    
    // 이제 쿠폰을 하나만 사용하니까 주문아이디만 필요
    @Modifying
    @Query("""
    UPDATE Coupon c
       SET c.status = :unusedStatus,
           c.usedAt = null,
           c.userOrderId = null
     WHERE c.status = :usedStatus
       AND c.userOrderId = :orderId
    """)
    int rollbackCouponsByOrderId(
            @Param("orderId") Long orderId,
            @Param("usedStatus") CouponStatus usedStatus,
            @Param("unusedStatus") CouponStatus unusedStatus
    );




    // 특정 정책에 속한 쿠폰들
    List<Coupon> findAllByCouponPolicy(CouponPolicy policy);

    // 정책 ID 기반 쿠폰 조회
    List<Coupon> findAllByCouponPolicy_PolicyId(Long policyId);

    Optional<Coupon> findByUserOrderId(Long orderId);

    List<Coupon> findTop5000ByStatusAndExpireAtBefore(CouponStatus status, LocalDateTime now);
}
