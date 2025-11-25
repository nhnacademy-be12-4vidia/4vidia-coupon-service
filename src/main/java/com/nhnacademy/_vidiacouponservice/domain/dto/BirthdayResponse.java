package com.nhnacademy._vidiacouponservice.domain.dto;

import java.time.LocalDate;

public record BirthdayResponse(Long userId, LocalDate birthDate) {}