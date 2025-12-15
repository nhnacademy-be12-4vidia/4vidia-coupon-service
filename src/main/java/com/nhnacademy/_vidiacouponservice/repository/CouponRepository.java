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

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 동시성 해결
    @Modifying
    @Query("""
    UPDATE Coupon c
       SET c.status = 'USED',
           c.usedAt = :usedAt,
           c.userOrderId = :orderId
     WHERE c.couponId = :couponId
       AND c.status = 'UNUSED'
       AND c.expireAt > :now
""")
    int useCouponIfUnusedAndNotExpired(
            Long couponId,
            Long orderId,
            LocalDateTime usedAt,
            LocalDateTime now
    );
    
    
    
    // 이제 쿠폰을 하나만 사용하니까 주문아이디만 필요
    @Modifying
    @Query("""
    UPDATE Coupon c
       SET c.status = 'UNUSED',
           c.usedAt = null,
           c.userOrderId = null
     WHERE c.status = 'USED'
       AND c.userOrderId = :orderId
""")
    int rollbackCouponsByOrderId(@Param("orderId") Long orderId);




    // 특정 정책에 속한 쿠폰들
    List<Coupon> findAllByCouponPolicy(CouponPolicy policy);

    // 정책 ID 기반 쿠폰 조회
    List<Coupon> findAllByCouponPolicy_PolicyId(Long policyId);

    List<Coupon> findAllByUserOrderId(Long orderId);

    List<Coupon> findTop5000ByStatusAndExpireAtBefore(CouponStatus status, LocalDateTime now);
}
