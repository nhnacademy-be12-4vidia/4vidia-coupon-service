package com.nhnacademy._vidiacouponservice.repository;


import com.nhnacademy._vidiacouponservice.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
