package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponValidateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.OrderCouponRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.OrderCouponResult;
import com.nhnacademy._vidiacouponservice.domain.dto.response.MyCouponPageResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.MyCouponResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.OrderPageCouponResponse;
import com.nhnacademy._vidiacouponservice.service.MyCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class MyCouponController {

    private final MyCouponService myCouponService;

    // 내 쿠폰 목록
    @GetMapping("/me")
    public MyCouponPageResponse getMyCoupons(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ALL") String status
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id.couponId") // 발급 최신순 비슷하게
        );

        return myCouponService.getMyCouponsPage(userId, status, pageable);
    }

    // 주문 화면 쿠폰 검증 리스트
    @PostMapping("/validate")
    public OrderCouponResult validateCoupons(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody OrderCouponRequest req
    ) {
        List<OrderPageCouponResponse> all =
                myCouponService.getOrderCoupons(userId, req);

        // boolean으로 사용가능,불가능 나눔
        Map<Boolean, List<OrderPageCouponResponse>> partition =
                all.stream()
                        .collect(Collectors.partitioningBy(OrderPageCouponResponse::available));

        return new OrderCouponResult(
                partition.getOrDefault(true, List.of()),
                partition.getOrDefault(false, List.of())
        );
    }

}
