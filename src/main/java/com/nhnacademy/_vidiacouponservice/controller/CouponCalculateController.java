package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponCalculationRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponCalculationResponse;
import com.nhnacademy._vidiacouponservice.service.CouponCalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/coupons")
public class CouponCalculateController {

    private final CouponCalculateService couponCalculateService;

    @PostMapping("/calculate")
    public CouponCalculationResponse calculate(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CouponCalculationRequest req
    ) {
        return couponCalculateService.calculate(userId, req);
    }
}
