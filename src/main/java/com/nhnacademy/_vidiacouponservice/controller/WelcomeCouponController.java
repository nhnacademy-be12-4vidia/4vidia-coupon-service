package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.service.WelcomeCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/coupons/welcome")
public class WelcomeCouponController {

    private final WelcomeCouponService welcomeCouponService;

    @PostMapping
    public void giveWelcome(@RequestHeader("X-User-Id") Long userId) {
        welcomeCouponService.giveWelcomeCoupon(userId);
    }
}
