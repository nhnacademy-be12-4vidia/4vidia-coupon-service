//package com.dooray.coupon.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class BirthdayScheduler {
//
//    private final BirthdayCouponService birthdayCouponService;
//
//    @Scheduled(cron = "0 0 0 1 * *")
//    public void runBirth(){
//        birthdayCouponService.giveBirthdayCoupon();
//    }
//}
