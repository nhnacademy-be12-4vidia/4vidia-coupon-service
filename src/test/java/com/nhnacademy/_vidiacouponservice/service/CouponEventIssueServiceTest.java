package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.client.UserClient;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.ValidityType;
import com.nhnacademy._vidiacouponservice.exception.DuplicateIssueRequestException;
import com.nhnacademy._vidiacouponservice.exception.PolicyInactiveException;
import com.nhnacademy._vidiacouponservice.exception.PolicyNotFoundException;
import com.nhnacademy._vidiacouponservice.producer.CouponIssueProducer;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import com.nhnacademy._vidiacouponservice.repository.UserCouponRepository;
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
class CouponEventIssueServiceTest {

    @InjectMocks
    CouponEventIssueService service;

    @Mock
    CouponPolicyRepository policyRepo;

    @Mock
    UserCouponRepository userCouponRepo;

    @Mock
    RedisTemplate<String, String> redis;

    @Mock
    UserClient userClient;

    @Mock
    CouponIssueProducer producer;

    @Mock
    ValueOperations<String, String> valueOps;

    @Test
    @DisplayName("성공: 중복없고 활성 정책이면 sendEvent 호출")
    void issueEvent_success() {
        Long userId = 1L;
        Long policyId = 10L;

        doNothing().when(userClient).validateUser(userId);

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyId(policyId);
        policy.setIsActivation(true);
        policy.setValidityType(ValidityType.RELATIVE);
        policy.setValidDays(5);

        when(policyRepo.findById(policyId)).thenReturn(Optional.of(policy));
        when(userCouponRepo.existsByIdUserIdAndPolicyId(userId, policyId)).thenReturn(false);

        when(redis.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), eq("1"))).thenReturn(true);

        assertThatCode(() -> service.issueEventCoupon(userId, policyId))
                .doesNotThrowAnyException();

        verify(producer).sendEvent(eq(userId), eq(policyId), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("실패: 정책 없음이면 PolicyNotFoundException")
    void issueEvent_policyNotFound() {
        Long userId = 1L;
        Long policyId = 99L;

        doNothing().when(userClient).validateUser(userId);
        when(policyRepo.findById(policyId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.issueEventCoupon(userId, policyId))
                .isInstanceOf(PolicyNotFoundException.class);

        verifyNoInteractions(producer);
    }

    @Test
    @DisplayName("실패: 비활성 정책이면 PolicyInactiveException")
    void issueEvent_inactive() {
        Long userId = 1L;
        Long policyId = 10L;

        doNothing().when(userClient).validateUser(userId);

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyId(policyId);
        policy.setIsActivation(false);

        when(policyRepo.findById(policyId)).thenReturn(Optional.of(policy));

        assertThatThrownBy(() -> service.issueEventCoupon(userId, policyId))
                .isInstanceOf(PolicyInactiveException.class);

        verifyNoInteractions(producer);
    }

    @Test
    @DisplayName("실패: DB 중복발급이면 DuplicateIssueRequestException")
    void issueEvent_duplicateByDb() {
        Long userId = 1L;
        Long policyId = 10L;

        doNothing().when(userClient).validateUser(userId);

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyId(policyId);
        policy.setIsActivation(true);
        policy.setValidityType(ValidityType.RELATIVE);
        policy.setValidDays(5);

        when(policyRepo.findById(policyId)).thenReturn(Optional.of(policy));
        when(userCouponRepo.existsByIdUserIdAndPolicyId(userId, policyId)).thenReturn(true);

        assertThatThrownBy(() -> service.issueEventCoupon(userId, policyId))
                .isInstanceOf(DuplicateIssueRequestException.class);

        verifyNoInteractions(producer);
    }

    @Test
    @DisplayName("실패: Redis 중복요청이면 DuplicateIssueRequestException")
    void issueEvent_duplicateByRedis() {
        Long userId = 1L;
        Long policyId = 10L;

        doNothing().when(userClient).validateUser(userId);

        CouponPolicy policy = new CouponPolicy();
        policy.setPolicyId(policyId);
        policy.setIsActivation(true);
        policy.setValidityType(ValidityType.RELATIVE);
        policy.setValidDays(5);

        when(policyRepo.findById(policyId)).thenReturn(Optional.of(policy));
        when(userCouponRepo.existsByIdUserIdAndPolicyId(userId, policyId)).thenReturn(false);

        when(redis.opsForValue()).thenReturn(valueOps);
        when(valueOps.setIfAbsent(anyString(), eq("1"))).thenReturn(false);

        assertThatThrownBy(() -> service.issueEventCoupon(userId, policyId))
                .isInstanceOf(DuplicateIssueRequestException.class);

        verifyNoInteractions(producer);
    }
}
