package com.dooray.coupon.domain.dto;

import java.time.LocalDate;

public record BirthdayResponse(Long userId, LocalDate birthDate) {}