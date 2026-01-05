package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.client.UserClient;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;
import com.nhnacademy._vidiacouponservice.exception.DuplicateIssueRequestException;
import com.nhnacademy._vidiacouponservice.exception.PolicyInactiveException;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotAdminIssuableException;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotFoundException;
import com.nhnacademy._vidiacouponservice.producer.CouponIssueProducer;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponIssueServiceTest {

    @InjectMocks
    CouponIssueService service;

    @Mock
    CouponPolicyRepository repo;

    @Mock
    RedisTemplate<String, String> redis;

    @Mock
    CouponIssueProducer producer;

    @Mock
    UserClient userClient;

    @Mock
    ValueOperations<String, String> valueOps;

    @Test
    @DisplayName("성공: EVENT 정책 + 활성 + setIfAbsent true면 MQ 발송")
    void issue_success() {
        Long userId = 1L;
        Long policyId = 2L;

        doNothing().when(userClient).validateUser(userId);

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyId(policyId);
        policy.setPolicyType(PolicyType.EVENT); // 웰컴/생일 아니어야 함
        policy.setIsActivation(true);
        policy.setValidityType(ValidityType.RELATIVE);
        policy.setValidDays(7);

        when(repo.findById(policyId)).thenReturn(Optional.of(policy));

        when(redis.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), eq("1"))).thenReturn(true);

        assertThatCode(() -> service.issue(userId, policyId)).doesNotThrowAnyException();

        verify(producer).sendIssue(eq(userId), eq(policyId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("실패: 정책 없음이면 PolicyNotFoundException")
    void issue_policyNotFound() {
        Long userId = 1L;
        Long policyId = 99L;

        doNothing().when(userClient).validateUser(userId);
        when(repo.findById(policyId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.issue(userId, policyId))
                .isInstanceOf(PolicyNotFoundException.class);

        verifyNoInteractions(producer);
    }

    @Test
    @DisplayName("실패: WELCOME/BIRTHDAY 정책이면 PolicyNotAdminIssuableException")
    void issue_blockWelcomeBirthday() {
        Long userId = 1L;
        Long policyId = 1L;

        doNothing().when(userClient).validateUser(userId);

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyId(policyId);
        policy.setPolicyType(PolicyType.WELCOME);
        policy.setIsActivation(true);

        when(repo.findById(policyId)).thenReturn(Optional.of(policy));

        assertThatThrownBy(() -> service.issue(userId, policyId))
                .isInstanceOf(PolicyNotAdminIssuableException.class);

        verifyNoInteractions(producer);
    }

    @Test
    @DisplayName("실패: 비활성 정책이면 PolicyInactiveException")
    void issue_inactive() {
        Long userId = 1L;
        Long policyId = 2L;

        doNothing().when(userClient).validateUser(userId);

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyId(policyId);
        policy.setPolicyType(PolicyType.EVENT);
        policy.setIsActivation(false);

        when(repo.findById(policyId)).thenReturn(Optional.of(policy));

        assertThatThrownBy(() -> service.issue(userId, policyId))
                .isInstanceOf(PolicyInactiveException.class);

        verifyNoInteractions(producer);
    }

    @Test
    @DisplayName("실패: setIfAbsent false면 DuplicateIssueRequestException")
    void issue_duplicateRequest() {
        Long userId = 1L;
        Long policyId = 2L;

        doNothing().when(userClient).validateUser(userId);

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyId(policyId);
        policy.setPolicyType(PolicyType.EVENT);
        policy.setIsActivation(true);
        policy.setValidityType(ValidityType.RELATIVE);
        policy.setValidDays(7);

        when(repo.findById(policyId)).thenReturn(Optional.of(policy));

        when(redis.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), eq("1"))).thenReturn(false);

        assertThatThrownBy(() -> service.issue(userId, policyId))
                .isInstanceOf(DuplicateIssueRequestException.class);

        verifyNoInteractions(producer);
    }
}
