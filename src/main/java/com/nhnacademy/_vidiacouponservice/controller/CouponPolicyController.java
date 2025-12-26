package com.nhnacademy._vidiacouponservice.controller;


import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponPolicyResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.PageResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.WelcomeCouponPolicy;
import com.nhnacademy._vidiacouponservice.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/welcome")
    public WelcomeCouponPolicy getWelcomeCouponPolicy() {
        CouponPolicy couponPolicy = policyService.find(1L);
        return new WelcomeCouponPolicy(couponPolicy.getDiscountValue());
    }

    @PatchMapping("/{policyId}/toggle")
    public void toggleActivation(@PathVariable Long policyId) {
        policyService.toggleActivation(policyId);
    }

    @GetMapping("/all")
    public List<CouponPolicy> findAll() {
        return policyService.findAll(); // 활성/비활성 모두
    }

    @GetMapping("/search")
    public PageResponse<CouponPolicyResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String targetType,
            Pageable pageable
    ) {
        DiscountTargetType type =
                targetType == null ? null : DiscountTargetType.valueOf(targetType);

        Page<CouponPolicyResponse> page =
                policyService.search(keyword, status, type, pageable)
                        .map(CouponPolicyResponse::from);

        return PageResponse.from(page);
    }


}