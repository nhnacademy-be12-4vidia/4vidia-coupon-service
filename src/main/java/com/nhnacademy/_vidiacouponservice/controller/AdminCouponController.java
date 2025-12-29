package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.response.CouponPolicyResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.MyCouponResponse;
import com.nhnacademy._vidiacouponservice.service.AdminCouponService;
import com.nhnacademy._vidiacouponservice.service.CouponEventIssueService;
import com.nhnacademy._vidiacouponservice.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminCouponController {

    private final AdminCouponService adminCouponService;
    private final CouponIssueService issueService;
    private final CouponEventIssueService eventIssueService;

    @GetMapping("/{userId}/coupons")
    public List<MyCouponResponse> getUserCoupons(@PathVariable Long userId) {
        return adminCouponService.getUserCoupons(userId);
    }

    @PostMapping("/{userId}/coupons/{policyId}/issue")
    public void adminIssue(
            @PathVariable Long userId,
            @PathVariable Long policyId
    ) {
        issueService.issue(userId, policyId);
    }

    @PostMapping("/{userId}/coupons/{policyId}/issue-event")
    public void adminEventIssue(
            @PathVariable Long userId,
            @PathVariable Long policyId
    ) {
        eventIssueService.issueEventCoupon(userId, policyId);
    }

    @GetMapping("/{userId}/coupons/issuable-policies")
    public List<CouponPolicyResponse> getIssuablePolicies(
            @PathVariable Long userId
    ) {
        return adminCouponService.getIssuablePolicies(userId)
                .stream()
                .map(CouponPolicyResponse::from)
                .toList();
    }


}
