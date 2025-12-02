package com.nhnacademy._vidiacouponservice.controller;


import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyUpdateRequest;
import com.nhnacademy._vidiacouponservice.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/policies")
public class CouponPolicyController {

    private final CouponPolicyService service;

    @PostMapping
    public CouponPolicy create(@RequestBody CouponPolicyCreateRequest dto) {
        return service.createPolicy(dto);
    }

    @GetMapping
    public Iterable<CouponPolicy> list() {
        return service.listPolicies();
    }

    @GetMapping("/{id}")
    public CouponPolicy find(@PathVariable Long id) {
        return service.findPolicy(id);
    }

    @PutMapping("/{id}")
    public CouponPolicy update(@PathVariable Long id,
                               @RequestBody CouponPolicyUpdateRequest dto) {
        return service.updatePolicy(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        service.deactivatePolicy(id);
    }

    @PatchMapping("/{id}/activation")
    public void activation(@PathVariable Long id,
                           @RequestBody Map<String, Boolean> body) {
        Boolean active = body.get("isActivation");
        if (active == null) throw new IllegalArgumentException("isActivation is required");
        service.changeActivePolicy(id, active);
    }
}
