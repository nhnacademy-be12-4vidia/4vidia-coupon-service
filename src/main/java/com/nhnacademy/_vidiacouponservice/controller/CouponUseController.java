package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponUseRequest;
import com.nhnacademy._vidiacouponservice.service.CouponUseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/coupons")
public class CouponUseController {

    private final CouponUseService couponUseService;

    @PostMapping("/use")
    public void useCoupon(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CouponUseRequest req
    ) {
        couponUseService.useCoupons(userId, req);
    }
}
