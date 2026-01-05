package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
import com.nhnacademy._vidiacouponservice.domain.UserCoupon;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.setting.CouponPolicyTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserCouponRepositoryTest {

    @Autowired
    UserCouponRepository repository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("유저는 특정 정책의 쿠폰을 이미 발급받았는지 확인할 수 있다")
    void existsByUserAndPolicy() {
        CouponPolicy policy =
                CouponPolicyTestFactory.activeEventAllPolicy();
        em.persist(policy);

        Coupon coupon = Coupon.issue(
                policy,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3)
        );
        em.persist(coupon);
        em.flush();

        em.persist(new UserCoupon(1L, coupon));
        em.flush();

        boolean exists =
                repository.existsByIdUserIdAndPolicyId(
                        1L,
                        policy.getPolicyId()
                );

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("유저가 보유한 UNUSED 쿠폰 개수를 조회할 수 있다")
    void countUnusedCoupons() {
        CouponPolicy policy =
                CouponPolicyTestFactory.activeEventAllPolicy();
        em.persist(policy);

        Coupon coupon = Coupon.issue(
                policy,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3)
        );
        em.persist(coupon);
        em.flush();

        em.persist(new UserCoupon(1L, coupon));
        em.flush();

        long count =
                repository.countByIdUserIdAndCoupon_Status(
                        1L,
                        CouponStatus.UNUSED
                );

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("유저가 발급받은 정책 ID 목록을 조회할 수 있다")
    void findPolicyIdsByUserId() {
        CouponPolicy policy =
                CouponPolicyTestFactory.activeEventAllPolicy();
        em.persist(policy);

        Coupon coupon = Coupon.issue(
                policy,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3)
        );
        em.persist(coupon);
        em.flush();

        em.persist(new UserCoupon(1L, coupon));
        em.flush();

        List<Long> policyIds =
                repository.findPolicyIdsByUserId(1L);

        assertThat(policyIds)
                .contains(policy.getPolicyId());
    }
}
