package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponValidateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.OrderCouponRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.MyCouponResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.OrderPageCouponResponse;
import com.nhnacademy._vidiacouponservice.service.MyCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class MyCouponController {

    private final MyCouponService myCouponService;

    // 내 쿠폰 목록
    @GetMapping("/me")
    public List<MyCouponResponse> getMyCoupons(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return myCouponService.getMyCoupons(userId);
    }

    // 주문 화면 쿠폰 검증 리스트
    @PostMapping("/validate")
    public List<OrderPageCouponResponse> validateCoupons(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody OrderCouponRequest req
    ) {
        return myCouponService.getOrderCoupons(userId, req);
    }
}
