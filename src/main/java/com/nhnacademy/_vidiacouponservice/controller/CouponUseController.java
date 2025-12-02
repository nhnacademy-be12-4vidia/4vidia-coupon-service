package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponUseRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponValidateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponValidateResponse;
import com.nhnacademy._vidiacouponservice.service.CouponUseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponUseController {

    private final CouponUseService couponUseService;

    @PostMapping("/coupons/{couponId}/validate")
    public CouponValidateResponse validateCoupon(@PathVariable Long couponId,
                                                 @RequestBody CouponValidateRequest couponValidateRequest) {
        return couponUseService.validateCoupon(couponId, couponValidateRequest);
    }


    @PostMapping("/coupons/{couponId}/use")
    public void use(
            @PathVariable Long couponId,
            @RequestBody CouponUseRequest couponUseRequest
    ){
        couponUseService.useCoupon(couponId, couponUseRequest);
    }
}
