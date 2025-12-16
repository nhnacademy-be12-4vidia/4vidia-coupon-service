package com.nhnacademy._vidiacouponservice.exception;

public class PolicyNotAdminIssuableException extends RuntimeException {
    public PolicyNotAdminIssuableException(Long policyId) {
        super("관리자 선착순 발급 대상이 아닌 정책입니다. policyId=" + policyId);
    }
}
