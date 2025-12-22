package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import com.nhnacademy._vidiacouponservice.domain.UserCouponId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, UserCouponId> {



    // 유저의 전체 쿠폰 조회 (내 쿠폰 목록)
    List<UserCoupon> findAllByIdUserId(Long userId);

    // 유저가 특정 쿠폰 보유 중인지 조회
    Optional<UserCoupon> findByIdUserIdAndIdCouponId(Long userId, Long couponId);

    // 유저가 특정 정책의 쿠폰을 이미 발급받았는지 확인 (웰컴/생일 같은 단일 발급 정책 체크)
    boolean existsByIdUserIdAndPolicyId(Long userId, Long policyId);

    // ✅ 추가: 유저가 이미 발급받은 "정책 ID 목록"
    @Query("""
        select distinct uc.policyId
        from UserCoupon uc
        where uc.id.userId = :userId
    """)
    List<Long> findPolicyIdsByUserId(@Param("userId") Long userId);
}
