package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.*;
import com.nhnacademy._vidiacouponservice.domain.dto.request.CouponPolicyCreateRequest;
import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponPolicyServiceTest {

    @InjectMocks
    CouponPolicyService service;

    @Mock
    CouponPolicyRepository repo;

    @Mock
    RedisTemplate<String, String> redis;

    @Mock
    HashOperations<String, Object, Object> hashOps;

    @Test
    @DisplayName("정책 생성 시 DB 저장 + Redis 수량 정보 초기화")
    void createPolicy_success() {
        // given
        when(redis.opsForHash()).thenReturn(hashOps);

        CouponPolicyCreateRequest req =
                new CouponPolicyCreateRequest(
                        "TEST",
                        PolicyType.EVENT,
                        DiscountType.PRICE,
                        DiscountTargetType.ALL,
                        1000,
                        null,
                        null,
                        ValidityType.RELATIVE,
                        7,
                        null,
                        null,
                        100,
                        0,
                        0,
                        true
                );

        CouponPolicy saved = new CouponPolicy();
        saved.setPolicyId(1L);
        saved.setLimitedQuantity(100);
        saved.setMinOrderAmount(0);
        saved.setMaxDiscountAmount(0);

        when(repo.save(any(CouponPolicy.class))).thenReturn(saved);

        // when
        CouponPolicy result = service.create(req);

        // then
        assertThat(result.getPolicyId()).isEqualTo(1L);

        verify(repo).save(any(CouponPolicy.class));
        verify(hashOps).put(anyString(), eq("stock"), eq("100"));
        verify(hashOps).put(anyString(), eq("issued"), eq("0"));
    }

    @Test
    @DisplayName("정책 활성화 상태를 토글할 수 있다")
    void toggleActivation() {
        // given
        CouponPolicy policy = new CouponPolicy();
        policy.setIsActivation(true);

        when(repo.findById(1L)).thenReturn(Optional.of(policy));

        // when
        service.toggleActivation(1L);

        // then
        assertThat(policy.getIsActivation()).isFalse();
        verify(repo).save(policy);
    }

    @Test
    @DisplayName("검색 로직을 Repository에 위임한다")
    void search_delegate() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        when(repo.search(null, null, null, pageable))
                .thenReturn(Page.empty());

        // when
        Page<CouponPolicy> result =
                service.search(null, null, null, pageable);

        // then
        assertThat(result).isNotNull();
        verify(repo).search(null, null, null, pageable);
    }
}
