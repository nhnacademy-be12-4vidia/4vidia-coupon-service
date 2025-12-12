package com.nhnacademy._vidiacouponservice.controller;


import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiacouponservice.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/policies")
public class CouponPolicyController {

    private final CouponPolicyService policyService;

    @PostMapping
    public CouponPolicy create(@RequestBody CouponPolicyCreateRequest req) {
        return policyService.create(req);
    }

    @GetMapping
    public List<CouponPolicy> findAllActive() {
        return policyService.findAllActive();
    }

    @GetMapping("/{policyId}")
    public CouponPolicy find(@PathVariable Long policyId) {
        return policyService.find(policyId);
    }

    @PatchMapping("/{policyId}/toggle")
    public void toggleActivation(@PathVariable Long policyId) {
        policyService.toggleActivation(policyId);
    }

    @GetMapping("/all")
    public List<CouponPolicy> findAll() {
        return policyService.findAll(); // 활성/비활성 모두
    }



}