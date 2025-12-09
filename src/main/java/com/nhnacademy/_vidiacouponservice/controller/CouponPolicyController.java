package com.nhnacademy._vidiacouponservice.controller;


import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyUpdateRequest;
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

    @PutMapping("/{policyId}")
    public CouponPolicy update(@PathVariable Long policyId,
                               @RequestBody CouponPolicyUpdateRequest req) {
        return policyService.update(policyId, req);
    }

    @GetMapping
    public List<CouponPolicy> findAllActive() {
        return policyService.findAllActive();
    }

    @GetMapping("/{policyId}")
    public CouponPolicy find(@PathVariable Long policyId) {
        return policyService.find(policyId);
    }

    @PatchMapping("/{policyId}/activate")
    public void activate(@PathVariable Long policyId) {
        policyService.activate(policyId);
    }

    @PatchMapping("/{policyId}/deactivate")
    public void deactivate(@PathVariable Long policyId) {
        policyService.deactivate(policyId);
    }

}