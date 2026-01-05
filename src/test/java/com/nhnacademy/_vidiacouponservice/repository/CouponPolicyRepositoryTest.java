package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.common.DiscountTargetType;
import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
import com.nhnacademy._vidiacouponservice.setting.CouponPolicyTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CouponPolicyRepositoryTest {

    @Autowired
    CouponPolicyRepository repository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("활성화된 쿠폰 정책만 조회할 수 있다")
    void findAllByIsActivationTrue() {
        em.persist(CouponPolicyTestFactory.activeEventAllPolicy());
        em.persist(CouponPolicyTestFactory.inactivePolicy());
        em.flush();

        List<CouponPolicy> result =
                repository.findAllByIsActivationTrue();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsActivation()).isTrue();
    }

    @Test
    @DisplayName("정책 타입과 활성화 여부로 단일 정책을 조회할 수 있다")
    void findByPolicyTypeAndIsActivationTrue() {
        CouponPolicy birthday =
                CouponPolicyTestFactory.birthdayPolicy();

        em.persist(birthday);
        em.flush();

        CouponPolicy found =
                repository.findByPolicyTypeAndIsActivationTrue(PolicyType.BIRTHDAY)
                        .orElseThrow();

        assertThat(found.getPolicyType())
                .isEqualTo(PolicyType.BIRTHDAY);
    }

}
