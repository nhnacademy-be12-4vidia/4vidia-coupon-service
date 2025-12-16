package com.nhnacademy._vidiacouponservice.exception;

public class DuplicateIssueRequestException extends RuntimeException {
    public DuplicateIssueRequestException(Long userId, Long policyId) {
        super("이미 발급 요청 처리중입니다. userId=" + userId + ", policyId=" + policyId);
    }
}
