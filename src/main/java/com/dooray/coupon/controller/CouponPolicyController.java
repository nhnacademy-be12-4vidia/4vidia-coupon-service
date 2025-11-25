package com.dooray.coupon.controller;

import com.dooray.coupon.domain.CouponPolicy;
import com.dooray.coupon.domain.dto.CouponPolicyUpdaterequest;
import com.dooray.coupon.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/policy")
public class CouponPolicyController {

    private final CouponPolicyService service;

    @PostMapping
    public CouponPolicy create(@RequestBody CouponPolicy dto) {
        return service.createPolicy(dto);
    }

    @PutMapping("/{id}")
    public CouponPolicy update(@PathVariable Long id,
                               @RequestBody CouponPolicyUpdaterequest dto) {
        return service.updatePolicy(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable Long id) {
        service.deactivatePolicy(id);
    }

    @PatchMapping("/{id}/activation")
    public void changActivation(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean active = body.get("isActivation");
        if(active == null) {
            throw new IllegalArgumentException("활성화값이 필요함");
        }
        service.changActivePolicy(id, active);
    }

    @GetMapping
    public Iterable<CouponPolicy> list() {
        return service.listPolicies();
    }

    @GetMapping("/{id}")
    public CouponPolicy find(@PathVariable Long id) {
        return service.findPolicy(id);
    }
}
