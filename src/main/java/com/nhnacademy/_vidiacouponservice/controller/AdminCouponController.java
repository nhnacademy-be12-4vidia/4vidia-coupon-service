package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.response.IssueResultResponse;
import com.nhnacademy._vidiacouponservice.domain.dto.response.MyCouponResponse;
import com.nhnacademy._vidiacouponservice.service.AdminCouponService;
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

    @GetMapping("/{userId}/coupons")
    public List<MyCouponResponse> getUserCoupons(@PathVariable Long userId) {
        return adminCouponService.getUserCoupons(userId);
    }

    @PostMapping("/{userId}/coupons/{policyId}/issue")
    public IssueResultResponse adminIssue(
            @PathVariable Long userId,
            @PathVariable Long policyId
    ) {
        issueService.issue(userId, policyId);
        return IssueResultResponse.ok(null);
    }

}
