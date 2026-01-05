package com.nhnacademy._vidiacouponservice.repository;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
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
class CouponRepositoryTest {

    @Autowired
    CouponRepository repository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("사용되지 않고 만료되지 않은 쿠폰은 사용 처리할 수 있다")
    void useCouponIfUnusedAndNotExpired() {
        CouponPolicy policy =
                CouponPolicyTestFactory.activeEventAllPolicy();
        em.persist(policy);

        Coupon coupon = Coupon.issue(
                policy,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        em.persist(coupon);
        em.flush();

        int updated =
                repository.useCouponIfUnusedAndNotExpired(
                        coupon.getCouponId(),
                        100L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        CouponStatus.UNUSED,
                        CouponStatus.USED
                );

        assertThat(updated).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 ID로 사용된 쿠폰을 롤백할 수 있다")
    void rollbackCouponsByOrderId() {
        CouponPolicy policy =
                CouponPolicyTestFactory.activeEventAllPolicy();
        em.persist(policy);

        Coupon coupon = Coupon.issue(
                policy,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        coupon.setStatus(CouponStatus.USED);
        coupon.setUserOrderId(200L);

        em.persist(coupon);
        em.flush();

        int rolledBack =
                repository.rollbackCouponsByOrderId(
                        200L,
                        CouponStatus.USED,
                        CouponStatus.UNUSED
                );

        assertThat(rolledBack).isEqualTo(1);
    }

    @Test
    @DisplayName("만료된 UNUSED 쿠폰을 최대 5000개까지 조회할 수 있다")
    void findExpiredCoupons() {
        CouponPolicy policy =
                CouponPolicyTestFactory.activeEventAllPolicy();
        em.persist(policy);

        Coupon coupon = Coupon.issue(
                policy,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(1)
        );
        em.persist(coupon);
        em.flush();

        List<Coupon> result =
                repository.findTop5000ByStatusAndExpireAtBefore(
                        CouponStatus.UNUSED,
                        LocalDateTime.now()
                );

        assertThat(result).isNotEmpty();
    }
}
