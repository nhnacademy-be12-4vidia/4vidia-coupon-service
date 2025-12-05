package com.nhnacademy._vidiacouponservice.service;

import com.nhnacademy._vidiacouponservice.domain.Coupon;
import com.nhnacademy._vidiacouponservice.domain.common.CouponStatus;
import com.nhnacademy._vidiacouponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponExpireScheduler {

    private final CouponRepository couponRepo;

    /**
     * 매일 새벽 0시에 만료된 쿠폰들을 'EXPIRED' 상태로 일괄 업데이트
     * 부하 방지를 위해 5000개 단위로 배치 처리
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void expireCoupons() {

        LocalDateTime now = LocalDateTime.now();
        int totalExpired = 0;

        while (true) {
            // 1) 한 번에 5000개씩 가져오기
            List<Coupon> expiredList = couponRepo
                    .findTop5000ByStatusAndExpireAtBefore(CouponStatus.UNUSED, now);

            if (expiredList.isEmpty()) {
                break; // 처리할 쿠폰 없음 → 종료
            }

            // 2) 상태 변경
            expiredList.forEach(c -> c.setStatus(CouponStatus.EXPIRED));

            // 3) DB 저장
            couponRepo.saveAll(expiredList);

            totalExpired += expiredList.size();
        }

        if (totalExpired > 0) {
            log.info("[만료처리] 만료된 쿠폰 {}개 업데이트 완료", totalExpired);
        }
    }
}
