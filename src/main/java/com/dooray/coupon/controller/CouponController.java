package com.dooray.coupon.controller;

import com.dooray.coupon.service.CouponService;
import com.dooray.coupon.service.WelcomeCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CouponController {
    private final CouponService couponService;
    private final WelcomeCouponService welcomeCouponService;

    @PostMapping("/{policyId}/issue")
    public ResponseEntity<?> issue(
            @PathVariable Long policyId,
            @RequestParam Long userId) {

        couponService.issue(userId, policyId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/welcome")
    public ResponseEntity<?> welcome(@RequestParam Long userId) {
        welcomeCouponService.giveWelcomeCoupon(userId);
        return ResponseEntity.accepted().build();
    }
}
