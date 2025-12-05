package com.nhnacademy._vidiacouponservice.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RabbitMQ가 전달할 메시지에 담길 데이터
 * @param userId(누가)
 * @param policyId(어떤정책)
 */
public record CouponIssueMessage(Long userId, Long policyId, LocalDateTime issuedAt, LocalDateTime expireAt) implements Serializable {
}
