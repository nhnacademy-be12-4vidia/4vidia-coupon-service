package com.nhnacademy._vidiacouponservice.repository;


import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import com.nhnacademy._vidiacouponservice.domain.UserCouponId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserCouponRepository extends JpaRepository<UserCoupon, UserCouponId> {
    Optional<UserCoupon> findByIdUserIdAndIdCouponId(Long userId, Long couponId);
}
