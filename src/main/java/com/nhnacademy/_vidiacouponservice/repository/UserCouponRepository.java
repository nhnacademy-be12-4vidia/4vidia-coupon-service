package com.nhnacademy._vidiacouponservice.repository;


import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

}
