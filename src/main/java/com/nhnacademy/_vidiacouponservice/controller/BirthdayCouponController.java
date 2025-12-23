package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.domain.dto.response.ActivePolicyIdResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponPolicyResponse;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import com.nhnacademy._vidiacouponservice.service.BirthdayCouponService;
import com.nhnacademy._vidiacouponservice.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/policies")
public class BirthdayCouponController {
    private final BirthdayCouponService birthdayCouponService;

    @GetMapping("/active")
    public ResponseEntity<ActivePolicyIdResponse> getActivePolicy(
            @RequestParam String policyType
    ) {
        return birthdayCouponService.findActivePolicy(
                        PolicyType.valueOf(policyType)
                )
                .map(policy -> ResponseEntity.ok(
                        new ActivePolicyIdResponse(policy.getPolicyId())
                ))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/birthday")
    public void giveBirthday(@RequestHeader("X-User-Id") Long userId) {
        birthdayCouponService.giveBirthdayCoupon(userId);
    }


}
