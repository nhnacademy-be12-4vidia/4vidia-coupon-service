//package com.nhnacademy._vidiacouponservice.service;
//
//import com.nhnacademy._vidiacouponservice.config.UserClient;
//import com.nhnacademy._vidiacouponservice.domain.CouponPolicy;
//import com.nhnacademy._vidiacouponservice.domain.common.PolicyType;
//import com.nhnacademy._vidiacouponservice.domain.dto.BirthdayResponse;
//import com.nhnacademy._vidiacouponservice.repository.CouponPolicyRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BirthdayCouponService {
//
//    private final CouponPolicyRepository policyRepo;
//    private final CouponService couponService;
//    private final UserClient userClient;
//
//    public void giveBirthdayCoupon() {
//        int month = LocalDate.now().getMonthValue();
//        List<BirthdayResponse> users = userClient.getBirthdayUsers(month);
//
//        if (users.isEmpty()) return;
//
//        CouponPolicy policy = policyRepo.findByPolicyType(PolicyType.BIRTHDAY)
//                .orElseThrow(() -> new IllegalArgumentException("BIRTHDAY 정책 없음"));
//
//        for (BirthdayResponse user : users) {
//            couponService.issue2(user.userId(), policy.getPolicyId());
//        }
//    }
//}
