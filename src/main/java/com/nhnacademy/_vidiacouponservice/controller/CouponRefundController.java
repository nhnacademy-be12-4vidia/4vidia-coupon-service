package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.request.RefundCouponRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.UseCouponResponse;
import com.nhnacademy._vidiacouponservice.service.CouponRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponRefundController {
    
    private final CouponRefundService couponRefundService;

    @PostMapping("/refund")
    public UseCouponResponse getUseCouponDetail(@RequestBody RefundCouponRequest req) {
        return couponRefundService.getUseCouponDetail(req.orderId());
    }

}
