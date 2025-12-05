package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.service.CouponUseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponRollbackController {

    private final CouponUseService couponUseService;

    @PostMapping("/rollback")
    public void rollback(@RequestBody Long orderId) {
        couponUseService.rollbackCoupons(orderId);
    }
}
