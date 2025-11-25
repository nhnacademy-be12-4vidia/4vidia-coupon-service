//package com.dooray.coupon.service;
//
//import com.dooray.coupon.config.UserClient;
//import com.dooray.coupon.domain.CouponPolicy;
//import com.dooray.coupon.domain.common.PolicyType;
//import com.dooray.coupon.domain.dto.BirthdayResponse;
//import com.dooray.coupon.repository.CouponPolicyRepository;
//import com.dooray.coupon.repository.CouponRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BirthdayCouponService {
//
//    private final CouponPolicyRepository couponPolicyRepository;
//    private final CouponService couponService;
//    private final UserClient userClient;
//
//    public void giveBirthdayCoupon(){
//        int month = LocalDate.now().getMonthValue();
//
//        List<BirthdayResponse> usersIds = userClient.getBirthdayUsers(month);
//
//        if(usersIds.isEmpty()){
//            return;
//        }
//
//        CouponPolicy policy = couponPolicyRepository.findByPolicyType(PolicyType.BIRTHDAY)
//                .orElseThrow(() -> new IllegalArgumentException("BIRTHDAY 정책 없음"));
//
//        for (BirthdayResponse userId : usersIds){
//            couponService.issue2(userId.userId(), policy.getPolicyId());
//        }
//    }
//}
