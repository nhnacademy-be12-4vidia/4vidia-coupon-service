package com.nhnacademy._vidiacouponservice.controller;

import com.nhnacademy._vidiacouponservice.domain.dto.response.IssueResultResponse;
import com.nhnacademy._vidiacouponservice.service.CouponEventIssueService;
import com.nhnacademy._vidiacouponservice.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponIssueController {

    private final CouponIssueService issueService;
    private final CouponEventIssueService eventIssueService;

    // 선착순 발급 (재고 있음)
    @PostMapping("/{policyId}/issue")
    public IssueResultResponse issue(
            @PathVariable Long policyId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        issueService.issue(userId, policyId);
        return IssueResultResponse.ok(null);
    }

    // 이벤트 / 웰컴 / 생일 (재고 없음)
    @PostMapping("/{policyId}/event-issue")
    public IssueResultResponse issueEvent(
            @PathVariable Long policyId,
            @RequestHeader("X-User-Id") Long userId
    ) {
        eventIssueService.issueEventCoupon(userId, policyId);
        return IssueResultResponse.ok(null);
    }
}
