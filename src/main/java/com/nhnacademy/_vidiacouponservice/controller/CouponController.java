package com.nhnacademy._vidiacouponservice.controller;


import com.nhnacademy._vidiacouponservice.service.CouponService;
import com.nhnacademy._vidiacouponservice.service.WelcomeCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final WelcomeCouponService welcomeCouponService;

    // 선착순 쿠폰 발급
    @PostMapping("/policies/{policyId}/issue")
    public void issue(@PathVariable Long policyId,
                      @RequestParam Long userId) {
        couponService.issue(userId, policyId);
    }

    // 웰컴 쿠폰 발급
    @PostMapping("/welcome")
    public void welcome(@RequestParam Long userId) {
        welcomeCouponService.giveWelcomeCoupon(userId);
    }
}
