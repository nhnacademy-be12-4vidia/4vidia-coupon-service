package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import com.nhnacademy._vidiacouponservice.domain.UserCouponId;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, UserCouponId> {

    // ✅ Page 전체 조회 (N+1 방지용 EntityGraph)
    @EntityGraph(attributePaths = {"coupon", "coupon.couponPolicy"})
    Page<UserCoupon> findAllByIdUserId(Long userId, Pageable pageable);

    // ✅ Page 상태 필터 조회 (UNUSED / USED / EXPIRED)
    @EntityGraph(attributePaths = {"coupon", "coupon.couponPolicy"})
    Page<UserCoupon> findAllByIdUserIdAndCoupon_Status(Long userId, CouponStatus status, Pageable pageable);

    // 기존: List 전체 조회 (내 쿠폰 목록)
    List<UserCoupon> findAllByIdUserId(Long userId);

    // 기존: 유저가 특정 쿠폰 보유 중인지 조회
    Optional<UserCoupon> findByIdUserIdAndIdCouponId(Long userId, Long couponId);

    // 기존: 유저가 특정 정책의 쿠폰을 이미 발급받았는지 확인
    boolean existsByIdUserIdAndPolicyId(Long userId, Long policyId);

    // ✅ 상단 “보유 쿠폰” 카운트 (UNUSED만)
    long countByIdUserIdAndCoupon_Status(Long userId, CouponStatus status);

    // ✅ 상단 “곧 만료” 카운트 (UNUSED + expireAt < now+7days)
    long countByIdUserIdAndCoupon_StatusAndCoupon_ExpireAtBefore(
            Long userId,
            CouponStatus status,
            LocalDateTime time
    );

    // 기존: 유저가 이미 발급받은 "정책 ID 목록"
    @Query("""
        select distinct uc.policyId
        from UserCoupon uc
        where uc.id.userId = :userId
    """)
    List<Long> findPolicyIdsByUserId(@Param("userId") Long userId);
}
